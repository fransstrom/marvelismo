package com.mrpwr.marvelismo.API

import com.google.gson.annotations.SerializedName

data class SerieList(@SerializedName("results") var series:ArrayList<Serie>) {

}
