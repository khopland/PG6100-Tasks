package org.tsdes.movies

import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.tsdes.movies.db.Movie
import org.tsdes.movies.dto.MovieDto
import javax.persistence.LockModeType


@Repository
interface MovieRepository : CrudRepository<Movie, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Movie m where m.id = :id")
    fun lockedFind(@Param("id") Id: String): Movie?
}

@Service
@Transactional
class MovieService(
    private val MovieRepository: MovieRepository
) {
    fun findByIdEager(id: String): Movie? {
        return MovieRepository.findById(id).orElse(null)
    }

    fun registerNewMovie(id: String, movieDto: MovieDto): Boolean {
        if (MovieRepository.existsById(id)) return false
        MovieRepository.save(Movie().apply {
            this.id = id
            title = movieDto.title
            director = movieDto.director
            year = movieDto.year
        })
        return true
    }
}