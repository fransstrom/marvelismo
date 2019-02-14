package com.mrpwr.marvelismo.registerlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mrpwr.marvelismo.R
import com.mrpwr.marvelismo.messages.LatestMessagesActivity
import com.mrpwr.marvelismo.models.User
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

  companion object {
    val TAG = "RegisterActivity"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)

    register_button_register.setOnClickListener {
      performRegister()
    }

    already_have_account_text_view.setOnClickListener {
      Log.d(TAG, "Try to show login activity")

      val intent = Intent(this, LoginActivity::class.java)
      startActivity(intent)
    }

    selectphoto_button_register.setOnClickListener {
      Log.d(TAG, "Try to show photo selector")

      val intent = Intent(Intent.ACTION_PICK)
      intent.type = "image/*"
      startActivityForResult(intent, 0)
    }
  }

  var selectedPhotoUri: Uri? = null

    //Choosing a picture
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
      Log.d(TAG, "Photo was selected")

      selectedPhotoUri = data.data

      val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

      selectphoto_imageview_register.setImageBitmap(bitmap)

      selectphoto_button_register.alpha = 0f
    }
  }

  private fun performRegister() {
    val email = email_edittext_register.text.toString()
    val password = password_edittext_register.text.toString()

    if (email.isEmpty() || password.isEmpty()) {
      Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
      return
    }

    Log.d(TAG, "Attempting to create user with email: $email")

    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d(TAG, "(NOT VERIFIED) Successfully created user with uid: ${it.result!!.user.uid}")

                //Sending mail for verification
                val user = FirebaseAuth.getInstance().currentUser
                user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "Email sent.")
                            }
                        }
                //Then the picture is uploaded
                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener{
                Log.d(TAG, "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
  }

  private fun uploadImageToFirebaseStorage() {
      //If the user didnt choose any picture they get a default picture
      val defaultProfileImgUrl: String = "https://firebasestorage.googleapis.com/v0/b/marvelismo-1337.appspot.com/o/images%2Fprofilepicture.png?alt=media&token=fe114062-87bb-43f6-8f0b-27f25fcd30a4"

      if (selectedPhotoUri == null){
        //Then save the user to the database with the defaultpicture
        saveUserToFirebaseDatabase(defaultProfileImgUrl)
        return
    }
    //otherwise if the user did select a picture
      //you give it a random UUID and place correctly with the reference under images. With firebase storage
    val filename = UUID.randomUUID().toString()
    val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

    ref.putFile(selectedPhotoUri!!)
        .addOnSuccessListener {
          Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

          ref.downloadUrl.addOnSuccessListener {
            Log.d(TAG, "File Location: $it")
            //Then if the picture is uploaded correctly, save the user in the database with their own picture
            saveUserToFirebaseDatabase(it.toString())
          }
        }
        .addOnFailureListener {
          Log.d(TAG, "Failed to upload image to storage: ${it.message}")
            //If the picture upload didnt work, still save the user with default profile picture
            //So the user data doesnt get lost if the upload failed.
            saveUserToFirebaseDatabase(defaultProfileImgUrl)

        }
  }

  private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
    val uid = FirebaseAuth.getInstance().uid ?: ""
    val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

    val user = User(uid, username_edittext_register.text.toString(), profileImageUrl)

      //Setting the user value
    ref.setValue(user)
        .addOnSuccessListener {
          Log.d(TAG, "Finally we saved the user to Firebase Database")
            //Going to the login activity
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        .addOnFailureListener {
          Log.d(TAG, "Failed to set value to database: ${it.message}")
        }
  }
}