package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.mrpwr.marvelismo.API.Hero
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.data.HeroListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hero_browse.*
import kotlinx.android.synthetic.main.comic_search_activity.*
import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class HeroBrowseActivity : AppCompatActivity() {

    var adapter: HeroListAdapter? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    var heroes=arrayListOf<Hero>()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_browse)


        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(okHttpClient)
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)

        var page: Int = intent.extras.getInt("PAGE")
        getHeroPage(service, page, 20)
        layoutManager = LinearLayoutManager(this)
        adapter = HeroListAdapter(heroes, this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView!!.canScrollVertically(1)) {
                    page++
                    getHeroPage(service, page, 20)
                }
            }
        })

    }

    @SuppressLint("CheckResult")
    fun getHeroPage(service: MarvelSevice, offset: Int, limit: Int) {
        var apiCredParams = MD5Hash()
        heroBrowseProgressBar.visibility = View.VISIBLE


        service.getHeroes(apiCredParams.apikey, apiCredParams.hash, apiCredParams.ts, (offset * limit), limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({

                if (it.result.heroes.size>0) {

                    for (hero in it.result.heroes) {
                        heroes.add(hero)
                    }
                    adapter!!.notifyDataSetChanged()

                    Toast.makeText(this, heroes.size.toString() + " heroes found", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "No heroes found", Toast.LENGTH_LONG).show()
                }

                heroBrowseProgressBar.visibility = View.INVISIBLE

            }, {

            })
    }


}
