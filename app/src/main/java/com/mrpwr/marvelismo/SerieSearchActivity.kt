package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.API.SeriesResponse
import com.mrpwr.marvelismo.data.ComicListAdapter
import com.mrpwr.marvelismo.data.HeroListAdapter
import com.mrpwr.marvelismo.data.SerieListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_serie_search.*
import kotlinx.android.synthetic.main.hero_search_activity.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SerieSearchActivity : AppCompatActivity() {
    var adapter: SerieListAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serie_search)

        seriesSearchProgressBar.visibility = View.INVISIBLE
        val searchView: SearchView = this.findViewById(R.id.serieSearchView) as SearchView
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //    callSearch(query)
                searchView.clearFocus()
                searchSerie(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
                //   callSearch(newText)
                //              }
                return true
            }

            fun callSearch(query: String) {
                //Do searching
            }
        })

    }

    @SuppressLint("CheckResult")
    private fun searchSerie(titleStartsWith: String) {
        seriesSearchProgressBar.visibility = View.VISIBLE
        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)
        var apiCredParams = MD5Hash()

        service.getSearchedSeries(apiCredParams.apikey, apiCredParams.hash, titleStartsWith, apiCredParams.ts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({

                var series = it.resultSeries.series
                if (series.size > 0) {
                    println(series)
                    layoutManager = LinearLayoutManager(this)
                    adapter = SerieListAdapter(series, this)
                    SerieRecyclerView.layoutManager = layoutManager
                    SerieRecyclerView.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                    Toast.makeText(this, series.size.toString() + " series found", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "No series found", Toast.LENGTH_LONG).show()
                }
                println(it.resultSeries)
                seriesSearchProgressBar.visibility = View.INVISIBLE


            }, {
                Toast.makeText(this,it.message.toString(),Toast.LENGTH_LONG).show()
            })
    }

    override fun onResume() {
        super.onResume()
        serieSearchView.isFocusable=false
    }
}
