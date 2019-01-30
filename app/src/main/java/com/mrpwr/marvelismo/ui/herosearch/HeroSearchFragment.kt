package com.mrpwr.marvelismo.ui.herosearch

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrpwr.marvelismo.R

class HeroSearchFragment : Fragment() {

    companion object {
        fun newInstance() = HeroSearchFragment()
    }

    private lateinit var viewModel: HeroSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.hero_search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HeroSearchViewModel::class.java)
        // TODO: Use the ViewModel
    }



}
