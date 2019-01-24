package com.mrpwr.marvelismo

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val apiCredParams = MD5Hash()

        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()





        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            println("HASCH!   " + apiCredParams.hash)

            val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)

            val call = service.getHeroes(apiCredParams.apikey, apiCredParams.hash, "spider", apiCredParams.ts)

            call.enqueue(object : Callback<HeroResponse> {
                override fun onFailure(call: Call<HeroResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "failed", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<HeroResponse>, response: Response<HeroResponse>) {
                    val body=response.body()
                   var result=body?.result

                    println(result?.heroes?.get(3))
                }

            })



//           var call= apiHeroes.getHeroes(apiCredParams.apikey, apiCredParams.hash, "spider", apiCredParams.ts)
//
//
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    println(it.data.heroes)
//                }, {
//                println("VA!")
//                })
        }

        var text = enterNameEdt.text

        showNameBtn.setOnClickListener {
            if (text.isEmpty()) {
                resultTxt.text = "Enter name"
            }
            resultTxt.text = "Welcome " + text
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
