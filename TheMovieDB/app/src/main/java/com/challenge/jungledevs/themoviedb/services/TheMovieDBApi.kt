package com.challenge.jungledevs.themoviedb.services

import com.challenge.jungledevs.themoviedb.model.*
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBApi {
    @GET("/3/genre/movie/list")
    fun getGenreList() : Call<GenresList>

    @GET("/3/movie/{id}")
    fun getMovie(@Path("id") id: Int) : Call<MovieDetails>

    @GET("/3/trending/movie/week")
    fun getTrendingMoviesWeek() : Call<MoviesList>

    @GET("/3/search/movie")
    fun searchMovies(@Query("query") query: String, @Query("page") page: Int = 1) : Call<MoviesList>
}