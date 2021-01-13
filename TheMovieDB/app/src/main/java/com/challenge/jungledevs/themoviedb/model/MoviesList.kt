package com.challenge.jungledevs.themoviedb.model

data class MoviesList(val page: Int = 0) {
    val results : List<Movie> = listOf<Movie>()
    val total_pages: Int = 0
    val total_results: Int = 0
}