package com.challenge.jungledevs.themoviedb.model

data class Movie(val id: Int = 0) {
    val title: String = ""
    val release_date: String = "0000-00-00"
    val genre_ids: List<Int> = listOf<Int>()
    val vote_average: Double = 0.0
    val poster_path: String = ""
    val overview: String = ""

    var vote: Int = 0
    var genres: String = ""
    var year: String = ""
    var poster: String = ""
    var top: Boolean = false
}