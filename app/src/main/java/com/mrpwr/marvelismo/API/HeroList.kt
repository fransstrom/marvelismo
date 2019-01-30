package com.mrpwr.marvelismo.API

import com.google.gson.annotations.SerializedName


data class HeroList(@SerializedName("results") var heroes:ArrayList<Hero>){

}