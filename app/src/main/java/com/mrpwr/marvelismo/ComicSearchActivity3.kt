package com.mrpwr.marvelismo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.SearchView
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.data.ComicListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hero_browse.*
import kotlinx.android.synthetic.main.comic_search_activity3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ComicSearchActivity3 : AppCompatActivity() {
    var adapter: ComicListAdapter ?= null
    private var layoutManager:RecyclerView.LayoutManager ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comic_search_activity3)



        val searchView: SearchView = this.findViewById(R.id.comicSearchView) as SearchView
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                //    callSearch(query)
                searchView.clearFocus()
                searchComics(query)
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


    fun searchComics( query : String ) {

        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)
        var apiCredParams = MD5Hash()

        service.getComicsObserv(apiCredParams.apikey, apiCredParams.hash, query, apiCredParams.ts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({
                println(it.resultComics)


                val comics = it.resultComics.comics
                println(comics)
                if (comics.size > 0) {

                    layoutManager= LinearLayoutManager(this)
                    adapter= ComicListAdapter(comics,this)

                    recyclerView3.layoutManager=layoutManager
                    recyclerView3.adapter=adapter

                    adapter!!.notifyDataSetChanged()
                }
            }, {

            })

    }



}
