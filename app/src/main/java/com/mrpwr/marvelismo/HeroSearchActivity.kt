package com.mrpwr.marvelismo

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mrpwr.marvelismo.API.Hero
import com.mrpwr.marvelismo.API.MD5Hash
import com.mrpwr.marvelismo.API.MarvelSevice
import com.mrpwr.marvelismo.data.HeroListAdapter
import com.mrpwr.marvelismo.messages.LatestMessagesActivity
import com.mrpwr.marvelismo.registerlogin.RegisterActivity
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

    var listLimit: Int = 0
    var page = 0

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
        adapter = HeroListAdapter(heroes, this)
        HeroSearchRecyclerView.layoutManager = layoutManager
        HeroSearchRecyclerView.adapter = adapter

        heroSearchProgressBar.visibility = View.INVISIBLE


        val searchView: SearchView = this.findViewById(R.id.heroSearchView) as SearchView
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()

        var nameStartWith: String? = null


        HeroSearchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView!!.canScrollVertically(1)) {
                    if (listLimit > heroes.size) {
                        page++
                        searchHeros(nameStartWith!!, service, 20, page)
                    } else {
                        println("No more heroes to get")
                    }
                }
            }
        })


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                heroes.clear()
                nameStartWith = query
                searchView.clearFocus()
                page = 0
                searchHeros(query, service, 20, page)
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

        service.getHeroesObserv(
            apiCredParams.apikey,
            apiCredParams.hash,
            query,
            apiCredParams.ts,
            offset * limit,
            limit
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({

                listLimit = it.result.total
                if (it.result.heroes.size > 0) {
                    for (hero in it.result.heroes) {
                        heroes.add(hero)
                    }
                    adapter!!.notifyDataSetChanged()
                    if (page == 0) {
                        Toast.makeText(this, listLimit.toString() + " heroes found", Toast.LENGTH_LONG).show()
                    }
                }

                heroSearchProgressBar.visibility = View.INVISIBLE

            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            })
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
