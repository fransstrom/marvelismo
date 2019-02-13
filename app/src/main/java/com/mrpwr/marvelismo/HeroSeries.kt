package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.API.Serie
import com.mrpwr.marvelismo.data.SerieListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hero_series.*
import kotlinx.android.synthetic.main.activity_serie_search.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.xml.datatype.DatatypeConstants.SECONDS
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


class HeroSeries : AppCompatActivity() {

    var adapter: SerieListAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    var page = 0
    var listLimit = 0
    var series = arrayListOf<Serie>()


    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .build()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_series)

        val heroId = intent.getStringExtra("HERO_ID")


        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(okHttpClient)
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)


        layoutManager = LinearLayoutManager(this)
        adapter = SerieListAdapter(series, this)
        heroSeriesRecycler.layoutManager = layoutManager
        heroSeriesRecycler.adapter = adapter


        heroSeriesRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView!!.canScrollVertically(1)) {
                    if (listLimit > series.size) {
                        page++
                        getHeroSerieList(service, heroId, page, 20)
                    } else {
                        println("No more heroes to get")
                    }
                }
            }
        })

        getHeroSerieList(service, heroId, page, 20)

    }

    @SuppressLint("CheckResult")
    private fun getHeroSerieList(service: MarvelSevice, heroId: String, offset: Int, limit: Int) {
        var apiCredParams = MD5Hash()
        heroSeriesProgressBar.visibility = View.VISIBLE
        service.getHeroSeries(heroId, apiCredParams.apikey, apiCredParams.hash, apiCredParams.ts, offset * limit, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({
                listLimit = it.resultSeries.total

                println("LIMIT FROM HEROSERIES "+listLimit)
                if (it.resultSeries.series.size > 0) {
                    for (serie in it.resultSeries.series) {
                        series.add(serie)
                    }
                    adapter!!.notifyDataSetChanged()

                    if (page == 0) {
                        Toast.makeText(this, listLimit.toString() + " series found", Toast.LENGTH_LONG).show()
                    }
                }
                println(it.resultSeries)
                heroSeriesProgressBar.visibility = View.INVISIBLE

            }, {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_homebtn, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home_btn -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}