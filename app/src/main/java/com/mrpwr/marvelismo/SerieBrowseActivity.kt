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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mrpwr.marvelismo.API.Hero
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.API.Serie
import com.mrpwr.marvelismo.data.HeroListAdapter
import com.mrpwr.marvelismo.data.SerieListAdapter
import com.mrpwr.marvelismo.messages.LatestMessagesActivity
import com.mrpwr.marvelismo.registerlogin.RegisterActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hero_browse.*
import kotlinx.android.synthetic.main.activity_serie_browse.*
import kotlinx.android.synthetic.main.activity_serie_search.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SerieBrowseActivity : AppCompatActivity() {

    var adapter: SerieListAdapter? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    var series = arrayListOf<Serie>()
    var page = 0
    var listLimit = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serie_browse)

        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(okHttpClient)
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)

        layoutManager = LinearLayoutManager(this)
        adapter = SerieListAdapter(series, this)
        recyclerBrowseSeries.layoutManager = layoutManager
        recyclerBrowseSeries.adapter = adapter

        getSeriesPage(service, page, 20)

        recyclerBrowseSeries.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView!!.canScrollVertically(1)) {
                    page++
                    getSeriesPage(service, page, 20)
                }
            }
        })

    }


    @SuppressLint("CheckResult")
    private fun getSeriesPage(service: MarvelSevice, offset: Int, limit: Int) {
        serieBrowseProgressBar.visibility = View.VISIBLE
        var apiCredParams = MD5Hash()
        service.getAllSeries(apiCredParams.apikey, apiCredParams.hash, apiCredParams.ts, offset * limit, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({
                listLimit = it.resultSeries.total

                if (it.resultSeries.series.size > 0) {
                    for (serie in it.resultSeries.series) {
                        series.add(serie)
                    }
                    adapter!!.notifyDataSetChanged()
                    if (page == 0) {
                        Toast.makeText(this, listLimit.toString() + " series found", Toast.LENGTH_LONG).show()
                    }
                }


                serieBrowseProgressBar.visibility = View.INVISIBLE

            }, {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_activities, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_signOut -> {
                FirebaseDatabase.getInstance().getReference("presence").child(FirebaseAuth.getInstance().currentUser!!.uid).removeValue()
                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("online").removeValue()
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                return true
            }
            R.id.action_searchHeroes -> {
                val intent = Intent(this, HeroSearchActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_browseHeroes -> {
                val intent = Intent(this, HeroBrowseActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_searchSeries -> {
                val intent = Intent(this, SerieSearchActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_browseSeries -> {
                val intent = Intent(this, SerieBrowseActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_friends -> {
                val intent = Intent(this, LatestMessagesActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
