package com.mrpwr.marvelismo.API

import com.google.gson.annotations.SerializedName

data class ComicResponse(@SerializedName("data") var resultComics:ComicList) {

}
