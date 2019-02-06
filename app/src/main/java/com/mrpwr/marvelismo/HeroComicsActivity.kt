package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.data.ComicListAdapter
import com.mrpwr.marvelismo.data.HeroListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hero_comics.*
import kotlinx.android.synthetic.main.hero_search_activity.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import android.widget.AdapterView




class HeroComicsActivity : AppCompatActivity() {
    var adapter: ComicListAdapter? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_hero_comics)
        val years = getYearList()
        val spinAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)

        val spinYear = findViewById(R.id.yearspin) as Spinner
        spinYear.adapter = spinAdapter

        val heroId = intent.getStringExtra("HERO_ID")

        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)
        var apiCredParams = MD5Hash()


        heroComics(service, heroId, apiCredParams)

        searchYear.setOnClickListener {
            heroComicsByYear(service, heroId, apiCredParams,spinYear.selectedItem.toString())
        }




//        yearspin?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                heroComics(service, heroId, apiCredParams)
//            }
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                println("ITEM SELECTED "+spinYear.selectedItem.toString())
//                heroComicsByYear(service, heroId, apiCredParams,spinYear.selectedItem.toString())
//            }
//        }
    }

    @SuppressLint("CheckResult")
    private fun heroComicsByYear(service: MarvelSevice, heroId: String, apiCredParams: MD5Hash, year: String) {
        service.getHeroComicsByYear(heroId, apiCredParams.apikey, apiCredParams.hash, apiCredParams.ts, 100, year)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({
                println(it.resultComics)
                val comics = it.resultComics.comics
                println(comics)
                if (comics.size > 0) {
                    layoutManager = LinearLayoutManager(this)
                    adapter = ComicListAdapter(comics, this)
                    recyclerHeroComicsView.layoutManager = layoutManager
                    recyclerHeroComicsView.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                }
            }, {
            })
    }


    @SuppressLint("CheckResult")
    private fun heroComics(service: MarvelSevice, heroId: String, apiCredParams: MD5Hash) {
        service.getHeroComics(heroId, apiCredParams.apikey, apiCredParams.hash, apiCredParams.ts, 100)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({
                println(it.resultComics)
                val comics = it.resultComics.comics
                println(comics)
                if (comics.size > 0) {
                    layoutManager = LinearLayoutManager(this)
                    adapter = ComicListAdapter(comics, this)
                    recyclerHeroComicsView.layoutManager = layoutManager
                    recyclerHeroComicsView.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                }
            }, {
            })
    }

    private fun getYearList(): ArrayList<String> {
        val years = ArrayList<String>()
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)

        for (i in 1939..thisYear) {
            years.add(Integer.toString(i))
        }
        years.reverse()
        return years
    }
}
