package org.tsdes.movies

import org.tsdes.movies.db.Movie
import org.tsdes.movies.db.Schedule
import org.tsdes.movies.dto.MovieDto
import org.tsdes.movies.dto.ScheduleDto

object DtoConverter {
    fun transform(movie: Movie): MovieDto = MovieDto().apply {
        id = movie.id
        title = movie.title
        director = movie.director
        year = movie.year
        schedule = movie.schedule.map { transform(it) }.toMutableList()
    }

    fun transform(schedule: Schedule): ScheduleDto = ScheduleDto().apply {
        time = schedule.time
        week = schedule.week
        roomNumber = schedule.roomNumber
    }
}