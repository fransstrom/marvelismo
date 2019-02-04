package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.data.HeroListAdapter
import com.mrpwr.marvelismo.ui.herosearch.HeroSearchFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.hero_search_activity.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class HeroSearchActivity : AppCompatActivity() {
    var adapter: HeroListAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hero_search_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HeroSearchFragment.newInstance())
                .commitNow()
        }


        heroSearchProgressBar.visibility= View.INVISIBLE


        val searchView: SearchView = this.findViewById(R.id.heroSearchView) as SearchView
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //    callSearch(query)
                searchView.clearFocus()
                searchHeros(query)
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

    }


    @SuppressLint("CheckResult")
    fun searchHeros(query: String) {
        heroSearchProgressBar.visibility= View.VISIBLE
        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)

        var apiCredParams = MD5Hash()

        service.getHeroesObserv(apiCredParams.apikey, apiCredParams.hash, query, apiCredParams.ts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({
                val heroes = it.result.heroes

                if (heroes.size > 0) {
                    layoutManager = LinearLayoutManager(this)
                    adapter = HeroListAdapter(heroes, this)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                    Toast.makeText(this, heroes.size.toString() + " heroes found", Toast.LENGTH_LONG).show()
                    heroSearchProgressBar.visibility= View.INVISIBLE
                }


            }, {

            })
    }


}
