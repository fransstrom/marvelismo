package com.mrpwr.marvelismo.API

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigInteger
import java.security.MessageDigest


interface MarvelSevice {
//    @GET("characters?={hero}")
//    fun listRepos(@Path("nameStartsWith") hero: String): Call<List<Hero>>


    @GET("/v1/public/characters")
    fun getHeroes(
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("nameStartsWith") nameStartsWith: String,
        @Query("ts")ts:String
    ): Call<HeroResponse>
}