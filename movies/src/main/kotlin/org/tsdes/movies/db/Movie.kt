package org.tsdes.movies.db

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.validation.constraints.NotBlank

@Entity
class Movie {
    @get:Id
    @get:NotBlank
    var id: String? = null

    @get:NotBlank
    var title: String? = null

    @get:NotBlank
    var director: String? = null

    @get:NotBlank
    var year: String? = null

    @get:OneToMany(mappedBy = "movie", cascade = [(CascadeType.ALL)])
    var schedule: MutableList<Schedule> = mutableListOf()

}