package com.mrpwr.marvelismo.API

data class Comic( var id:Int = 0, var title:String, var description:String , var thumbnail:ThumbNail, var urls:ArrayList<ComicUrl>, var images:ArrayList<Images>, var dates:ArrayList<PublishedDate> ) {

}