package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.data.HeroListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hero_series.*
import kotlinx.android.synthetic.main.activity_serie_hero_list.*
import kotlinx.android.synthetic.main.hero_search_activity.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SerieHeroListActivity : AppCompatActivity() {
    var adapter: HeroListAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serie_hero_list)

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()



        serieHeroesProgressbar.visibility = View.VISIBLE
        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(okHttpClient)
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)

        var apiCredParams = MD5Hash()

        val serieId = intent.getStringExtra("SERIE_ID")
        val serieTitle = intent.getStringExtra("SERIE_TITLE")

//        serieHeroListTitle.text = "Heroes staring in $serieTitle"

        service.getSerieHeroes(serieId, apiCredParams.apikey, apiCredParams.hash, apiCredParams.ts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({
                val heroes = it.result.heroes
                if (heroes.size > 0) {
                    layoutManager = LinearLayoutManager(this)
                    adapter = HeroListAdapter(heroes, this)
                    serieHeroesRecyclerView.layoutManager = layoutManager
                    serieHeroesRecyclerView.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                    Toast.makeText(this, heroes.size.toString() + " heroes found", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "No heroes found", Toast.LENGTH_LONG).show()

                }

                serieHeroesProgressbar.visibility = View.INVISIBLE

            }, {

                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            })


    }
}
