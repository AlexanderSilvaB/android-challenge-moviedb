package com.challenge.jungledevs.themoviedb.viewmodel

import com.challenge.jungledevs.themoviedb.model.Movie

interface NavigationInterface {
    fun onMovieClick(movie: Movie)
    fun onSearchClick()
    fun tryAgain(page: String)
}