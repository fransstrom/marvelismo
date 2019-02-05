package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.data.HeroListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hero_browse.*

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class HeroBrowseActivity : FragmentActivity() {

    var adapter: HeroListAdapter? = null
    var layoutManager: RecyclerView.LayoutManager? = null


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_browse)

        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)


        val page: Int = intent.extras.getInt("PAGE")

        nextPage.setOnClickListener {
            val intent = Intent(this, HeroBrowseActivity::class.java)
            intent.putExtra("PAGE", page+1)
            startActivity(intent)
        }
        nextPage.visibility = View.INVISIBLE
        pageNr.visibility=View.INVISIBLE
        prevPage.visibility=View.INVISIBLE
        pageNr.text=(page+1).toString()
        getHeroPage(service, page, 100)


    }

    @SuppressLint("CheckResult")
    fun getHeroPage(service: MarvelSevice, offset: Int, limit:Int) {
        var apiCredParams = MD5Hash()

        service.getHeroes(apiCredParams.apikey, apiCredParams.hash, apiCredParams.ts, (offset*limit), limit)
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
                } else {
                    Toast.makeText(this, "No heroes found", Toast.LENGTH_LONG).show()
                }
                nextPage.visibility = View.VISIBLE
                heroSearchProgressBar.visibility = View.INVISIBLE
                pageNr.visibility=View.VISIBLE
                prevPage.visibility=View.VISIBLE
            }, {

            })
    }


}
