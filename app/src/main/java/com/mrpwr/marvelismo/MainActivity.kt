package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.StringBuilder
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mrpwr.marvelismo.messages.LatestMessagesActivity
import com.mrpwr.marvelismo.models.User
import com.mrpwr.marvelismo.registerlogin.RegisterActivity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener




class MainActivity : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)




        heroSearchActivityBtn2.setOnClickListener {
            val intent = Intent(this, HeroSearchActivity::class.java)
            startActivity(intent)
        }


        searchComicsBtn.setOnClickListener {
            val intent = Intent(this, SerieSearchActivity::class.java)
            startActivity(intent)
        }

        openChatBtn.setOnClickListener{
            val intent = Intent(this, LatestMessagesActivity::class.java)
            startActivity(intent)
        }

        browseHerobtn.setOnClickListener {
            val intent = Intent(this, HeroBrowseActivity::class.java)
            intent.putExtra("PAGE", 0)
            startActivity(intent)
        }

        browse_seroes_btn.setOnClickListener {
            startActivity(Intent(this,SerieBrowseActivity::class.java))
        }



        verifyUserIsLoggedIn()
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            //if the user is not logged in go back to register activity.
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }else{
            //if the user is online, set their presence to true
            val amOnline = FirebaseDatabase.getInstance().getReference(".info").child("connected")
            val userPresenceRef = FirebaseDatabase.getInstance().getReference("presence").child(uid)
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("online")

            amOnline.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val connected = snapshot.getValue(Boolean::class.java) ?: false
                    if (connected) {
                        userPresenceRef.setValue(true)
                        userPresenceRef.onDisconnect().removeValue()
                        userRef.setValue(true)
                        userRef.onDisconnect().removeValue()
                    }
                }
            })
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null ) {

            val myRef = FirebaseDatabase.getInstance().getReference("users").child(user.uid)

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.getValue(User::class.java)
                    menu.findItem(R.id.user_logged_in).title =value!!.username
                    menu.findItem(R.id.user_logged_in2).title ="User: " + value!!.username

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
        return super.onPrepareOptionsMenu(menu)
    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    private fun byteArrayToHexString(array: Array<Byte>): String {
        var result = StringBuilder(array.size * 2)
        for (byte in array) {
            val toAppend = String.format("%2X", byte).replace(" ", "0")

            result.append(toAppend)
        }
        return result.toString().toLowerCase()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_signOut -> {
                //When logged out remove online status
                FirebaseDatabase.getInstance().getReference("presence").child(FirebaseAuth.getInstance().currentUser!!.uid).removeValue()
                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("online").removeValue()
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }
}
