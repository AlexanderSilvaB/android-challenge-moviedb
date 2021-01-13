package com.challenge.jungledevs.themoviedb.services

import com.challenge.jungledevs.themoviedb.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class TheMovieDBDataSource(val api: TheMovieDBApi) {
    fun getGenres(success: (List<Genre>) -> Unit, failure: () -> Unit){
        val call = api.getGenreList()
        call.enqueue(object : Callback<GenresList>{
            override fun onResponse(call: Call<GenresList>, response: Response<GenresList>) {
                if (response.isSuccessful) {
                    val genres = mutableListOf<Genre>()
                    response.body()?.genres?.forEach {
                        genres.add(Genre(it.id, it.name))
                    }
                    success(genres)
                } else {
                    failure()
                }
            }

            override fun onFailure(call: Call<GenresList>, t: Throwable?) {
                failure()
            }
        })
    }

    fun getTrending(genres: List<Genre>, success: (List<Movie>) -> Unit, failure: () -> Unit){
        val call = api.getTrendingMoviesWeek()
        call.enqueue(object : Callback<MoviesList>{
            override fun onResponse(call: Call<MoviesList>, response: Response<MoviesList>) {
                if (response.isSuccessful) {
                    val movies = mutableListOf<Movie>()
                    response.body()?.results?.forEachIndexed { i, movie ->
                        processMovie(movie, i, genres)
                        movies.add(movie)
                    }
                    success(movies)
                } else {
                    failure()
                }
            }

            override fun onFailure(call: Call<MoviesList>, t: Throwable?) {
                failure()
            }
        })
    }

    fun searchMovies(query: String, page: Int = 0, genres: List<Genre>, success: (List<Movie>) -> Unit, failure: () -> Unit){
        val call = api.searchMovies(query, page)
        call.enqueue(object : Callback<MoviesList>{
            override fun onResponse(call: Call<MoviesList>, response: Response<MoviesList>) {
                if (response.isSuccessful) {
                    val movies = mutableListOf<Movie>()
                    response.body()?.results?.forEachIndexed { i, movie ->
                        processMovie(movie, -1, genres)
                        movies.add(movie)
                    }
                    success(movies)
                } else {
                    failure()
                }
            }

            override fun onFailure(call: Call<MoviesList>, t: Throwable?) {
                failure()
            }
        })
    }

    fun getMovie(id: Int, genres: List<Genre>, movies: List<Movie>, success: (MovieDetails) -> Unit, failure: () -> Unit){
        val call = api.getMovie(id)
        call.enqueue(object : Callback<MovieDetails>{
            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                if (response.isSuccessful) {
                    if(response.body() != null) {
                        val movie = response.body() as MovieDetails
                        processMovieDetails(movie, movies, genres)
                        success(movie)
                    }
                    else
                        failure()
                } else {
                    failure()
                }
            }

            override fun onFailure(call: Call<MovieDetails>, t: Throwable?) {
                failure()
            }
        })
    }

    private fun processMovie(movie: Movie, index: Int, genres: List<Genre>){
        val movieGenres = StringBuilder()
        movie.genre_ids.forEachIndexed { j, item ->
            val genre = genres.find { item == it.id }
            if(genre != null) {
                movieGenres.append(genre.name)
                if(j < (movie.genre_ids.size - 1))
                    movieGenres.append(" / ")
            }
        }
        movie.genres = movieGenres.toString()
        movie.vote = (movie.vote_average / 2).toInt()
        try {
            movie.year = movie.release_date.substring(0, 4)
        }catch (e : Exception){
            movie.year = "Unknown"
        }
        movie.poster = "https://image.tmdb.org/t/p/w500" + movie.poster_path
        movie.top = (index == 0)
    }

    private fun processMovieDetails(movie: MovieDetails, movies: List<Movie>, genres: List<Genre>){
        val movieGenres = StringBuilder()
        movie.genres.forEachIndexed { j, item ->
            movieGenres.append(item.name)
            if(j < (movie.genres.size - 1))
                movieGenres.append(" / ")
        }
        movie.genres_string = movieGenres.toString()
        movie.vote = (movie.vote_average / 2).toInt()
        try {
            movie.year = movie.release_date.substring(0, 4)
        }catch (e: Exception){
            movie.year = "Unknown"
        }

        movie.poster = "https://image.tmdb.org/t/p/w500" + movie.poster_path

        val hours = movie.runtime / 60
        val minutes = movie.runtime - (hours * 60)
        movie.duration = String.format("%dh %dm", hours, minutes)

        if(movies.size == 0)
            movie.top = false
        else
            movie.top = movies[0].id == movie.id
    }
}