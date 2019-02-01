package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import com.mrpwr.marvelismo.API.HeroUrl
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_hero_view.*
import kotlinx.android.synthetic.main.content_hero_view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class HeroViewActivity : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_view)
        setSupportActionBar(toolbar)




        val message:String = intent.getStringExtra("HERO_ID")
        println(message.toString() + "FROM HERO VIEW")
        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)

        var apiCredParams = MD5Hash()

        service.getHero(message,apiCredParams.apikey, apiCredParams.hash,  apiCredParams.ts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({
                val heroes = it.result.heroes
                val hero = heroes[0]
                val img=hero.thumbnail.path.plus(".").plus(hero.thumbnail.extension)

                if (heroes.size > 0) {
                    Picasso.get().load(img).resize(550, 550).centerCrop().into(heroViewImg)
                    heroTitle.text=hero.name
                    heroViewDescription.text=hero.description
                    println("FROM HERO VIEWS "+ hero.urls)

                    val wikiObj: List<HeroUrl> =hero.urls.filter { e-> e.type=="wiki"}

                    val wikiUrl:String=wikiObj[0].url


                        println("WIKIURL:::: "+wikiUrl)

                    val wikiWebIntent:Intent=Intent(this, HeroWikiActivity::class.java)
                    wikiWebIntent.putExtra("WIKI_URL",wikiUrl)
                    heroWikiBtn.setOnClickListener{
                        startActivity(wikiWebIntent)
                    }
                }


            }, {

            })

    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()


    }

}
