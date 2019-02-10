package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mrpwr.marvelismo.API.HeroUrl
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.API.SerieUrls
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hero_view.*
import kotlinx.android.synthetic.main.activity_serie_search.*
import kotlinx.android.synthetic.main.activity_serie_view.*
import kotlinx.android.synthetic.main.hero_search_activity.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SerieViewActivity : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serie_view)

        val serieId=intent.getStringExtra("SERIE_ID")

        println(serieId)
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()


        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(okHttpClient)
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)

        var apiCredParams = MD5Hash()
        serieViewProgressBar.visibility= View.VISIBLE
        service.getSerie(serieId, apiCredParams.apikey, apiCredParams.hash, apiCredParams.ts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({
                val series = it.resultSeries.series
                val serie = series[0]
                val img = serie.thumbnail.path.plus(".").plus(serie.thumbnail.extension)
                if (series.size > 0) {
                    Picasso.get().load(img).resize(serieViewImg.width, serieViewImg.width).centerCrop().into(serieViewImg)
                    serieTitle.text = serie.title
                    serieViewDescription.text = serie.description

                    println("FROM HERO VIEWS " + serie.urls)
                    val url: String
                    val detailUrl: SerieUrls? = serie.urls.find { e -> e.type == "detail" }
                    url = detailUrl?.url.toString()

//                    val wikiWebIntent: Intent = Intent(this, HeroWikiActivity::class.java)


//                    val heroSeriesIntent: Intent = Intent(this, HeroSeries::class.java)
//                    heroSeriesIntent.putExtra("HERO_ID", hero.id.toString())


//                    println("Wikiobj from herovie " + (wikiUrl))

//                    heroSeriesBtn.setOnClickListener {
//                        startActivity(heroSeriesIntent)
//                    }

//                    if (!(wikiUrl === "null")) {
//                        wikiWebIntent.putExtra("WIKI_URL", wikiUrl)
//                        heroWikiBtn.setOnClickListener {
//                            startActivity(wikiWebIntent)
//                        }
//                    } else {
//                        Toast.makeText(this, "NO WIKI PAGE FOUND", Toast.LENGTH_LONG).show()
//                        heroWikiBtn.visibility = View.INVISIBLE
//                    }


                    val heroListIntent :Intent=Intent(this, SerieHeroListActivity::class.java)
                    heroListIntent.putExtra("SERIE_ID", serieId)
                    heroListIntent.putExtra("SERIE_TITLE",serie.title)
                    serieHeroesBtn.setOnClickListener {
                        startActivity(heroListIntent)
                    }



                }

                serieViewProgressBar.visibility= View.INVISIBLE
            }, {
                serieViewProgressBar.visibility= View.INVISIBLE
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            })



    }


}
