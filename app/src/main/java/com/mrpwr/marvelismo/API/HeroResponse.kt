package com.mrpwr.marvelismo.API

import com.google.gson.annotations.SerializedName

data class HeroResponse (@SerializedName("data") var result:HeroList){

}