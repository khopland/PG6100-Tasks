package org.tsdes.movies

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.tsdes.advanced.rest.dto.PageDto
import org.tsdes.advanced.rest.dto.RestResponseFactory
import org.tsdes.advanced.rest.dto.WrappedResponse
import org.tsdes.movies.dto.MovieDto
import java.util.concurrent.TimeUnit

@Api(value = "/api/movies", description = "Operations on movies")
@RequestMapping(
    "/api/movies",
    produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
@RestController
class RestAPI(private val movieService: MovieService) {

    @ApiOperation("get a Movie by an id")
    @GetMapping("/{id}")
    fun getMovieInfo(@PathVariable("id") id: String): ResponseEntity<WrappedResponse<MovieDto>> {
        val movie = movieService.findByIdEager(id) ?: return RestResponseFactory.notFound("movie $id not found")
        return RestResponseFactory.payload(200, DtoConverter.transform(movie))
    }

    @ApiOperation("creat a user on a id")
    @PutMapping("/{id}")
    fun createUser(@PathVariable("id") id: String, @RequestBody dto: MovieDto): ResponseEntity<WrappedResponse<Void>> {
        return if (movieService.registerNewMovie(id, dto)) RestResponseFactory.noPayload(201)
        else RestResponseFactory.userFailure("User $id already exist")
    }

    @ApiOperation("Return an iterable page of movies")
    @GetMapping
    fun getAll(
        @ApiParam("Id of player in the previous page")
        @RequestParam("keysetId", required = false)
        keysetId: String?
    ): ResponseEntity<WrappedResponse<PageDto<MovieDto>>> {
        val n = 10
        val page = PageDto<MovieDto>().apply {
            list = DtoConverter.transform(movieService.getNextPage(n, keysetId))
        }
        if (page.list.size == n)
            page.next = "/api/movies?keysetId=${page.list.last().id}"

        return ResponseEntity
            .status(200)
            .cacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES).cachePublic())
            .body(WrappedResponse(200, page).validated())
    }
}

