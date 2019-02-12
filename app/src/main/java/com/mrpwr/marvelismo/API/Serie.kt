package com.mrpwr.marvelismo.API

data class Serie(val id:Int, val title:String, val description:String, val thumbnail:ThumbNail, val urls:ArrayList<SerieUrls>) {

}
