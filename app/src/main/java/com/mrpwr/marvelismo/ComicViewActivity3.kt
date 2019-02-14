package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mrpwr.marvelismo.API.ComicUrl
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.messages.LatestMessagesActivity
import com.mrpwr.marvelismo.registerlogin.RegisterActivity
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hero_view.*
import kotlinx.android.synthetic.main.comic_view_activity3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ComicViewActivity3 : AppCompatActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comic_view_activity3)


        val message: String = intent.getStringExtra("COMIC_ID")
        val retroFit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val service: MarvelSevice = retroFit.create(MarvelSevice::class.java)

        var apiCredParams = MD5Hash()

        service.getComic(message, apiCredParams.apikey, apiCredParams.hash, apiCredParams.ts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({
                val comics = it.resultComics.comics
                val comic = comics[0]
//                val img = comic.thumbnail.path.plus(".").plus(comic.thumbnail.extension)
                val image = comic.images[0].path.plus(".").plus(comic.images[0].extension)

                println(comic)

                if (comics.size > 0) {
                    Picasso.get().load(image).resize(comicViewImg.width, comicViewImg.height).centerCrop().into(comicViewImg)
                    comicTitle.text = comic.title
                    comicPublished.text = "Published: " + comic.dates[0].date.substring(0, 10)
                    comicViewDescription.text = comic.description
                    println("FROM Comic VIEWS " + comic.urls)


//                    val wikiUrl: String
//                    val wikiObj: ComicUrl? = comic.urls.find { e -> e.type == "wiki" }
//                    wikiUrl = wikiObj?.url.toString()
//                    val wikiWebIntent: Intent = Intent(this, ComicWikiActivity::class.java)
//
//                    println("Wikiobj from herovie " + (wikiUrl))
//
//                    if (!(wikiUrl === "null")) {
//                        wikiWebIntent.putExtra("WIKI_URL", wikiUrl)
//                        heroWikiBtn.setOnClickListener {
//                            startActivity(wikiWebIntent)
//                        }
//                    } else {
//                        Toast.makeText(this, "NO WIKI PAGE FOUND", Toast.LENGTH_LONG).show()
//                        heroWikiBtn.visibility = View.INVISIBLE
//                    }
                }


            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            })

    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()


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