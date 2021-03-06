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
    fun getHeroesObserv(
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("nameStartsWith") nameStartsWith: String,
        @Query("ts") ts: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Observable<HeroResponse>


    @GET("/v1/public/comics")
    fun getComicsObserv(
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("titleStartsWith") nameStartsWith: String,
        @Query("ts") ts: String
    ): Observable<ComicResponse>



    @GET("/v1/public/comics/{comicId}")
    fun getComic(
        @Path("comicId") comicId : String,
        @Query("apikey") apikey : String,
        @Query("hash") hash : String,
        @Query("ts") ts : String
    ): Observable<ComicResponse>

    @GET("/v1/public/characters/{characterId}")
    fun getHero(
        @Path("characterId") characterId: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String
    ): Observable<HeroResponse>


    @GET("/v1/public/characters")
    fun getHeroes(
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Observable<HeroResponse>


    @GET("/v1/public/series")
    fun getSearchedSeries(
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("titleStartsWith") titleStartsWith: String,
        @Query("ts") ts: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Observable<SeriesResponse>


    @GET("/v1/public/characters/{characterId}/series")
    fun getHeroSeries(
        @Path("characterId") characterId: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("contains") contains: String = "comic",
        @Query("orderBy")orderBy:String="-startYear"
    ): Observable<SeriesResponse>

    @GET("/v1/public/series/{seriesId}")
    fun getSerie(
        @Path("seriesId") seriesId: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String
    ): Observable<SeriesResponse>


    @GET("/v1/public/series/{seriesId}/characters")
    fun getSerieHeroes(
        @Path("seriesId") seriesId: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Observable<HeroResponse>

    @GET("/v1/public/series")
    fun getAllSeries(
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Observable<SeriesResponse>



    @GET("/v1/public/series/{seriesId}/comics")
    fun getSerieComics(
        @Path("seriesId")seriesId: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("orderBy")orderBy:String ="issueNumber"
    ): Observable<ComicResponse>
}