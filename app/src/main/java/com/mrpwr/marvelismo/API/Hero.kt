package com.mrpwr.marvelismo.API

data class Hero(var id:Int=0, var name: String, var description:String, var thumbnail:ThumbNail, var urls: ArrayList<HeroUrl>) {

}
