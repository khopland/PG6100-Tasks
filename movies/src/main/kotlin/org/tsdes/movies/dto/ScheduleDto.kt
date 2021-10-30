package org.tsdes.movies.dto

import io.swagger.annotations.ApiModelProperty
import java.sql.Time
import java.time.DayOfWeek

data class ScheduleDto(
    @get:ApiModelProperty("The time of the movie")
    var time: Time? = null,

    @get:ApiModelProperty("The day of the week the movie is going")
    var dayOfWeek: DayOfWeek? = null,

    @get:ApiModelProperty("the week number its on")
    var week: Int = 0,

    @get:ApiModelProperty("the room number")
    var roomNumber: Int = 0
)