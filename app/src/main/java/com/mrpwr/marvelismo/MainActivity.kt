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
import com.mrpwr.marvelismo.messages.LatestMessagesActivity
import com.mrpwr.marvelismo.registerlogin.RegisterActivity


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

        verifyUserIsLoggedIn()
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
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
