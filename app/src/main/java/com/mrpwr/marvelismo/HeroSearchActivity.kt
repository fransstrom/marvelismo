package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import com.mrpwr.marvelismo.API.Hero
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.data.HeroListAdapter
import com.mrpwr.marvelismo.ui.herosearch.HeroSearchFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.hero_search_activity.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class HeroSearchActivity : AppCompatActivity(){
    var adapter:HeroListAdapter?=null
    private var layoutManager:RecyclerView.LayoutManager?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hero_search_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HeroSearchFragment.newInstance())
                .commitNow()
        }



        goBack.setOnClickListener { view ->
    startActivity(Intent(this, MainActivity::class.java))

        }



    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()


        val message = intent.getStringExtra("SEARCH_VALUE")
        println(message)
        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)
        var apiCredParams = MD5Hash()

        service.getHeroesObserv(apiCredParams.apikey, apiCredParams.hash, message, apiCredParams.ts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({
                val heroes = it.result.heroes
                println(heroes)
                if (heroes.size > 0) {
                    layoutManager=LinearLayoutManager(this)
                    adapter=HeroListAdapter(heroes,this)
                    recyclerView.layoutManager=layoutManager
                    recyclerView.adapter=adapter
                    adapter!!.notifyDataSetChanged()
                }
           Toast.makeText(this,heroes.size.toString() + "heroes found", Toast.LENGTH_LONG)

            }, {

            })
    }


}
