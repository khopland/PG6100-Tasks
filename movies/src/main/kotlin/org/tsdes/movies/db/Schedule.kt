package org.tsdes.movies.db

import java.sql.Time
import java.time.DayOfWeek
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
class Schedule {
    @get:Id
    @get:GeneratedValue
    var id: Long? = null

    @get:ManyToOne
    @get:NotNull
    var movie: Movie? = null

    @get:NotNull
    var time: Time? = null

    @get:NotNull
    var dayOfWeek: DayOfWeek? = null

    @get:Min(1)
    @get:Max(52)
    var week: Int = 0

    @get:Min(0)
    var roomNumber: Int = 0
}