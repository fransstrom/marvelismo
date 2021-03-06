package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.data.ComicListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.comic_search_activity.*
import kotlinx.android.synthetic.main.hero_search_activity.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ComicSearchActivity : AppCompatActivity() {

    var adapter : ComicListAdapter ?= null
    private var layoutManager: RecyclerView.LayoutManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comic_search_activity)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }


//    @SuppressLint("CheckResult")
//    override fun onResume() {
//        super.onResume()
//
//
//        val message = intent.getStringExtra("SEARCH_VALUE")
//        println(message)
//        val retroFit = Retrofit.Builder()
//            .baseUrl("https://gateway.marvel.com")
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .build()
//        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)
//        var apiCredParams = MD5Hash()
//
//        service.getComicsObserv(apiCredParams.apikey, apiCredParams.hash, message, apiCredParams.ts)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .unsubscribeOn(Schedulers.io())
//            .subscribe({
//
//                val comics = it.resultComics.comics
//                println(comics)
//                if (comics.size > 0) {
//
//                    layoutManager= LinearLayoutManager(this)
//                    adapter = ComicListAdapter(comics,this)
//
//                    recyclerView.layoutManager = layoutManager
//                    recyclerView.adapter = adapter
//
//                    adapter!!.notifyDataSetChanged()
//                }
//            }, {
//
//            })
//    }

}
