package com.challenge.jungledevs.themoviedb.viewmodel

import android.content.Context
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.challenge.jungledevs.themoviedb.R
import com.challenge.jungledevs.themoviedb.model.Genre
import com.challenge.jungledevs.themoviedb.model.Movie
import com.challenge.jungledevs.themoviedb.model.MovieDetails
import com.challenge.jungledevs.themoviedb.services.TheMovieDBDataSource

class MoviesListViewModel(val data_source: TheMovieDBDataSource, val context: Context) {
    private val genres = ObservableArrayList<Genre>()
    val movies = ObservableArrayList<Movie>()
    val searchResults = ObservableArrayList<Movie>()

    val loadingVisibility = ObservableBoolean(false)
    val errorVisibility = ObservableBoolean(false)
    val message = ObservableField<String>("")

    val searchQuery = ObservableField<String>("")
    val searchPage = ObservableField<Int>(1)
    val selected = ObservableField<MovieDetails>()
    val nonSelectedMovies = ObservableArrayList<Movie>()

    fun reset(){
        loadingVisibility.set(false)
        message.set("")
        errorVisibility.set(false)
    }

    private fun loadGenres(success: () -> Unit, failure: () -> Unit){
        if(genres.size > 0) {
            success()
            return
        }
        data_source.getGenres({items ->
            genres.clear()
            genres.addAll(items)
            if(items.isEmpty()){
                failure()
            }
            success()
        }, {
            failure()
        })
    }

    private fun loadMovies(success: () -> Unit, failure: () -> Unit){
        data_source.getTrending(genres, { items ->
            movies.clear()
            movies.addAll(items)
            if(items.isEmpty()){
                failure()
            }
            success()
        }, {
            failure()
        })
    }

    private fun searchMovies(query: String, page: Int, success: () -> Unit, failure: () -> Unit){
        val clear = searchQuery.get() != query
        searchQuery.set(query)
        searchPage.set(page)
        data_source.searchMovies(query, page, genres, { items ->
            if(clear)
                searchResults.clear()
            searchResults.addAll(items)
            success()
        }, {
            failure()
        })
    }

    private fun loadMovie(id :Int, success: () -> Unit, failure: () -> Unit){
        data_source.getMovie(id, genres, movies, { item ->
            selected.set(item)
            success()
        }, {
            failure()
        })
    }

    fun load() {
        errorVisibility.set(false)
        loadingVisibility.set(true)
        message.set("")
        loadGenres({
            loadMovies({
                loadingVisibility.set(false)
            }, {
                message.set(context.getString(R.string.movies_list_failure))
                loadingVisibility.set(false)
                errorVisibility.set(true)
            })
        }, {
            message.set(context.getString(R.string.movies_list_failure))
            loadingVisibility.set(false)
            errorVisibility.set(true)
        })
    }

    fun search(query: String, page: Int = 1){
        if(loadingVisibility.get())
            return
        loadingVisibility.set(true)
        message.set("")
        errorVisibility.set(false)
        if(query.length == 0)
        {
            loadingVisibility.set(false)
        }
        else {
            loadGenres({
                searchMovies(query, page, {
                    loadingVisibility.set(false)
                }, {
                    if(searchResults.size == 0) {
                        message.set(context.getString(R.string.movies_search_failure))
                        loadingVisibility.set(false)
                        errorVisibility.set(true)
                    }
                })
            }, {
                message.set(context.getString(R.string.movies_search_failure))
                loadingVisibility.set(false)
                errorVisibility.set(true)
            })
        }
    }

    fun searchMore(){
        if(searchQuery.get() != null && searchPage.get() != null)
            search(searchQuery.get() as String, (searchPage.get() as Int) + 1)
    }

    fun loadSelected(callback: (success: Boolean) -> Unit ){
        loadingVisibility.set(true)
        errorVisibility.set(false)
        message.set("")
        loadGenres({
            loadMovies({
                selected.get()?.id?.let {
                    loadMovie(it, {
                        loadingVisibility.set(false)
                        select(selected.get()!!)
                        callback(true)
                    }, {
                        message.set(context.getString(R.string.movie_load_failure))
                        loadingVisibility.set(false)
                        errorVisibility.set(true)
                        callback(false)
                    })
                }
            }, {
                message.set(context.getString(R.string.movie_load_failure))
                loadingVisibility.set(false)
                errorVisibility.set(true)
                callback(false)
            })
        }, {
            message.set(context.getString(R.string.movie_load_failure))
            loadingVisibility.set(false)
            errorVisibility.set(true)
            callback(false)
        })
    }

    fun select(movie: Movie){

        val movieDetails = MovieDetails(movie.id)
        movieDetails.genres_string = movie.genres
        movieDetails.title = movie.title
        movieDetails.poster = movie.poster
        movieDetails.overview = movie.overview
        movieDetails.year = movie.year
        movieDetails.top = movie.top
        movieDetails.vote = movie.vote


        selected.set(movieDetails)
        nonSelectedMovies.clear()
        nonSelectedMovies.addAll(movies)
        nonSelectedMovies.remove(movie)
    }

    fun select(movieDetails: MovieDetails){
        selected.set(movieDetails)
        nonSelectedMovies.clear()
        nonSelectedMovies.addAll(movies)
        nonSelectedMovies.remove(Movie(movieDetails.id))
    }
}