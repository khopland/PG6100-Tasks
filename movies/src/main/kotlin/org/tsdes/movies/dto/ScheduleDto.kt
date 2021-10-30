package org.tsdes.movies.dto

import io.swagger.annotations.ApiModelProperty
import java.sql.Time
import java.time.DayOfWeek

data class ScheduleDto(
    @get:ApiModelProperty("The time of the movie")
    var time: Time? = null,

    @get:ApiModelProperty("The day of the week the movie is going")
    var week: DayOfWeek? = null,

    @get:ApiModelProperty("the room number")
    var roomNumber: Int = 0
)