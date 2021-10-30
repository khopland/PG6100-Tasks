package org.tsdes.movies.dto

import io.swagger.annotations.ApiModelProperty

data class MovieDto(

    @get:ApiModelProperty("The id of the movie")
    var id: String? = null,

    @get:ApiModelProperty("Title of the movie")
    var title: String? = null,

    @get:ApiModelProperty("The director of the movie")
    var director: String? = null,

    @get:ApiModelProperty("The year fo the movie")
    var year: String? = null,

    @get:ApiModelProperty("List of cards owned by the user")
    var schedule: MutableList<ScheduleDto> = mutableListOf()
)