package com.mrpwr.marvelismo.registerlogin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mrpwr.marvelismo.MainActivity
import com.mrpwr.marvelismo.R
import com.mrpwr.marvelismo.messages.LatestMessagesActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_login)

    login_button_login.setOnClickListener {
      performLogin()
    }

    back_to_register_textview.setOnClickListener{
      finish()
    }
  }

  private fun performLogin() {
    val email = email_edittext_login.text.toString()
    val password = password_edittext_login.text.toString()

    if (email.isEmpty() || password.isEmpty()) {
      Toast.makeText(this, "Please fill out email/pw.", Toast.LENGTH_SHORT).show()
      return
    }

    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener {
          if (!it.isSuccessful) return@addOnCompleteListener

          Log.d("Login", "Successfully logged in: ${it.result!!.user.uid}")

          val user = FirebaseAuth.getInstance().currentUser;
            if(user != null && user.isEmailVerified) {
                val amOnline = FirebaseDatabase.getInstance().getReference(".info").child("connected")
                val userRef = FirebaseDatabase.getInstance().getReference("presence").child(user.uid)
                val userRef2 = FirebaseDatabase.getInstance().getReference("users").child(user.uid).child("online")
                amOnline.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val connected = snapshot.getValue(Boolean::class.java) ?: false
                        if (connected) {
                            userRef.setValue(true)
                            userRef.onDisconnect().removeValue()
                            userRef2.setValue(true)
                            userRef2.onDisconnect().removeValue()
                        }
                    }
                })
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Email not verified", Toast.LENGTH_SHORT).show()

            }
        }
        .addOnFailureListener {
          Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
        }
  }

}