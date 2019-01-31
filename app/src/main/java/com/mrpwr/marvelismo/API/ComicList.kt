package com.mrpwr.marvelismo.API

import com.google.gson.annotations.SerializedName

data class ComicList(@SerializedName("results") var comics:ArrayList<Comic>) {

}