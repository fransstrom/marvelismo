package com.mrpwr.marvelismo.API

import com.google.gson.annotations.SerializedName

data class SeriesResponse(@SerializedName("data") var resultSeries:SerieList){}