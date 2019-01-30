package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import com.mrpwr.marvelismo.API.Hero
import com.mrpwr.marvelismo.API.HeroResponse
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.lang.StringBuilder
import java.security.MessageDigest
import android.content.Intent
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.hero_search_activity.*


class MainActivity : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


//        val retroFit = Retrofit.Builder()
//            .baseUrl("https://gateway.marvel.com")
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .build()
//
//        var apiCredParams = MD5Hash()
//        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)
//        var heroAdapter: ArrayAdapter<String>


        fab.setOnClickListener { view ->


        }



        showNameBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, HeroSearchActivity::class.java))
            val editText = findViewById<EditText>(R.id.enterNameEdt)
            val message = editText.text.toString()
            val intent = Intent(this, HeroSearchActivity::class.java).apply {
                putExtra("SEARCH_VALUE", message)
            }
            startActivity(intent)


//            val search = enterNameEdt.text
//            if (search.isEmpty() || search.length < 3) {
//              //  resultTxt.text = getString(R.string.Enter3chars)
//            } else {
//                service.getHeroesObserv(apiCredParams.apikey, apiCredParams.hash, search.toString(), apiCredParams.ts)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .unsubscribeOn(Schedulers.io())
//                    .subscribe({
//                        val heroes = it.result.heroes
//                        println(heroes)
////                        var showString: String? = null
//                        if (heroes.size > 0) {
////                            showString = "Welcome " + heroes.get(0).name
////                            resultTxt.text = showString
//                            var heroNames: MutableList<String> = ArrayList()
//
//                            for(hero in heroes){
//                                heroNames.add(hero.name)
//                            }
//
//                            heroAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, heroNames)
//                            hero_list.adapter = heroAdapter
//                        }
//                    }, {
//
//                    })


// FUNKAR BRA MEN OBSERVABLE ÄR SNABBARE
//                call.clone().enqueue(object : Callback<HeroResponse> {
//                    override fun onFailure(call: Call<HeroResponse>, t: Throwable) {
//                        Toast.makeText(applicationContext, "failed", Toast.LENGTH_LONG).show()
//                    }
//
//                    override fun onResponse(call: Call<HeroResponse>, response: Response<HeroResponse>) {
//                        val body = response.body()
//                        var result = body?.result
//                        var heroes = result?.heroes
//                        println(heroes)
//                        var showString: String? = null
//                        if (heroes?.size!! > 0) {
//                            showString = "Welcome " + result?.heroes?.get(0)?.name
//                            resultTxt.text = showString
//                        }
//                    }
//                })
            //      }
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
