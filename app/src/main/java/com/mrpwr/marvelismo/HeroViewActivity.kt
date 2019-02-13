package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.mrpwr.marvelismo.API.HeroUrl
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_hero_view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class HeroViewActivity : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_view)

        val message: String = intent.getStringExtra("HERO_ID")
        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)


        var apiCredParams = MD5Hash()
        heroViewProgressBar.visibility=View.VISIBLE
        service.getHero(message, apiCredParams.apikey, apiCredParams.hash, apiCredParams.ts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({
                val heroes = it.result.heroes
                val hero = heroes[0]
                val img = hero.thumbnail.path.plus(".").plus(hero.thumbnail.extension)

                if (heroes.size > 0) {
                    Picasso.get().load(img).resize(heroViewImg.width, heroViewImg.width).centerCrop().into(heroViewImg)
                    heroTitle.text = hero.name
                    heroViewDescription.text = hero.description
                    println("FROM HERO VIEWS " + hero.urls)


                    val wikiUrl: String
                    val wikiObj: HeroUrl? = hero.urls.find { e -> e.type == "wiki" }
                    wikiUrl = wikiObj?.url.toString()
                    val wikiWebIntent: Intent = Intent(this, HeroWikiActivity::class.java)


                    val heroSeriesIntent: Intent = Intent(this, HeroSeries::class.java)
                    heroSeriesIntent.putExtra("HERO_ID", hero.id.toString())


                    println("Wikiobj from herovie " + (wikiUrl))

                    heroSeriesBtn.setOnClickListener {
                        startActivity(heroSeriesIntent)
                    }

                    if (!(wikiUrl === "null")) {
                        wikiWebIntent.putExtra("WIKI_URL", wikiUrl)
                        heroWikiBtn.setOnClickListener {
                            startActivity(wikiWebIntent)
                        }
                    } else {
                        Toast.makeText(this, "NO WIKI PAGE FOUND", Toast.LENGTH_LONG).show()
                        heroWikiBtn.visibility = View.INVISIBLE
                    }
                }

                heroViewProgressBar.visibility=View.INVISIBLE
            }, {
                heroViewProgressBar.visibility=View.INVISIBLE

                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            })

    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
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
