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
import android.widget.SearchView
import android.widget.Toast
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.API.Serie
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


    var page=0
    var series= arrayListOf<Serie>()
    var listLimit=0
    var searchText=""

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serie_search)

        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)

        layoutManager = LinearLayoutManager(this)
        adapter = SerieListAdapter(series, this)
        SerieRecyclerView.layoutManager = layoutManager
        SerieRecyclerView.adapter = adapter


        seriesSearchProgressBar.visibility = View.INVISIBLE
        val searchView: SearchView = this.findViewById(R.id.serieSearchView) as SearchView
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //    callSearch(query)
                searchView.clearFocus()
                searchText=query
                searchSerie(searchText,page, 20,service)
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

        SerieRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView!!.canScrollVertically(1)) {
                    if (listLimit > series.size) {
                        series.clear()
                        page++
                        searchSerie(searchText, page, 20, service)
                    } else {
                        println("No more heroes to get")
                    }
                }
            }
        })

    }

    @SuppressLint("CheckResult")
    private fun searchSerie(titleStartsWith: String, offset:Int,limit:Int, service:MarvelSevice) {
        seriesSearchProgressBar.visibility = View.VISIBLE

        var apiCredParams = MD5Hash()

        service.getSearchedSeries(apiCredParams.apikey, apiCredParams.hash, titleStartsWith, apiCredParams.ts,page*offset,limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({
                listLimit=it.resultSeries.total

                if (it.resultSeries.series.size > 0) {
                    for (serie in it.resultSeries.series) {
                        series.add(serie)
                    }
                    adapter!!.notifyDataSetChanged()
                    if (page == 0) {
                        Toast.makeText(this, listLimit.toString() + " series found", Toast.LENGTH_LONG).show()
                    }
                }

                seriesSearchProgressBar.visibility = View.INVISIBLE

            }, {
                Toast.makeText(this,it.message.toString(),Toast.LENGTH_LONG).show()
            })
    }

    override fun onResume() {
        super.onResume()
        serieSearchView.isFocusable=false
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
