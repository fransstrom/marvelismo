package com.mrpwr.marvelismo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mrpwr.marvelismo.ui.herosearch.HeroSearchFragment

class HeroSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hero_search_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HeroSearchFragment.newInstance())
                .commitNow()
        }
    }

}
