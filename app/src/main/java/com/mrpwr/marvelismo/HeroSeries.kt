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
import com.mrpwr.marvelismo.data.SerieListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hero_series.*
import kotlinx.android.synthetic.main.activity_serie_search.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class HeroSeries : AppCompatActivity() {

    var adapter: SerieListAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_series)

        heroSeriesProgressBar.visibility = View.VISIBLE
        val heroId = intent.getStringExtra("HERO_ID")

        println("HEROID FROM HEROSERIES " + heroId)


        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)

        var apiCredParams = MD5Hash()

        service.getHeroSeries(heroId, apiCredParams.apikey, apiCredParams.hash, apiCredParams.ts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({
                println("should have results!!!! DASDGHFJHGDSFADsdfghfdgs")
                var series = it.resultSeries.series
                if (series.size > 0) {
                    println(series)
                    layoutManager = LinearLayoutManager(this)
                    adapter = SerieListAdapter(series, this)
                    heroSeriesRecycler.layoutManager = layoutManager
                    heroSeriesRecycler.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                    Toast.makeText(this, series.size.toString() + " series found", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "No series found", Toast.LENGTH_LONG).show()
                }
                println(it.resultSeries)
                heroSeriesProgressBar.visibility = View.INVISIBLE

            }, {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            })

    }
}