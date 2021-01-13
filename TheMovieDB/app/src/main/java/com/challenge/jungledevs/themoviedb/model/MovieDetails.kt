package com.challenge.jungledevs.themoviedb.model

data class MovieDetails(val id: Int = 0) {
    var title: String = ""
    var release_date: String = "0000-00-00"
    var genres: List<Genre> = listOf<Genre>()
    var vote_average: Double = 0.0
    var poster_path: String = ""
    var overview: String = ""
    var runtime: Int = 0

    var vote: Int = 0
    var genres_string: String = ""
    var year: String = ""
    var duration: String = ""
    var poster: String = ""
    var top: Boolean = false
}