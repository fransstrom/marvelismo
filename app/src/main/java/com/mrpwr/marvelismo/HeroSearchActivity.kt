package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import com.mrpwr.marvelismo.API.Hero
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.data.HeroListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.flowable.FlowableLimit
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hero_browse.*
import kotlinx.android.synthetic.main.hero_search_activity.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class HeroSearchActivity : AppCompatActivity() {
    var adapter: HeroListAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    var heroes = arrayListOf<Hero>()


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hero_search_activity)

        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)

        layoutManager = LinearLayoutManager(this)
        adapter = HeroListAdapter(heroes!!, this)
        HeroSearchRecyclerView.layoutManager = layoutManager
        HeroSearchRecyclerView.adapter = adapter


        heroSearchProgressBar.visibility = View.INVISIBLE


        val searchView: SearchView = this.findViewById(R.id.heroSearchView) as SearchView
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()

        var nameStartWith:String?=null
        var page:Int=0

        HeroSearchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView!!.canScrollVertically(1)) {
                    page++
                    searchHeros(nameStartWith!!,service, page, 20)
                }
            }
        })


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //    callSearch(query)
                heroes.clear()
                nameStartWith=query
                searchView.clearFocus()
                page=0

                searchHeros(query, service,20,0)
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


    override fun onResume() {
        super.onResume()
        heroSearchView.isFocusable = false
    }


    @SuppressLint("CheckResult")
    fun searchHeros(query: String, service: MarvelSevice, limit: Int, offset: Int) {
        heroSearchProgressBar.visibility = View.VISIBLE
        var apiCredParams = MD5Hash()

        service.getHeroesObserv(apiCredParams.apikey, apiCredParams.hash, query, apiCredParams.ts, offset*limit, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({

                if (it.result.heroes.size > 0) {

                    for (hero in it.result.heroes) {
                        heroes.add(hero)
                    }
                    adapter!!.notifyDataSetChanged()

                    Toast.makeText(this, heroes.size.toString() + " heroes found", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "No heroes found", Toast.LENGTH_LONG).show()
                }

                heroSearchProgressBar.visibility = View.INVISIBLE


            }, {

            })
    }


}
