package org.tsdes.movies

import io.restassured.RestAssured
import io.restassured.RestAssured.*
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.tsdes.advanced.rest.dto.PageDto
import org.tsdes.movies.dto.MovieDto
import javax.annotation.PostConstruct

@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(
    classes = [(Application::class)],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
internal class RestApiTest @Autowired constructor(private val repository: MovieRepository) {

    @LocalServerPort
    protected var port = 0

    @PostConstruct
    fun init() {
        baseURI = "http://localhost"
        RestAssured.port = port
        basePath = "/api/movies"
        enableLoggingOfRequestAndResponseIfValidationFails()
    }

    var page: Int = 3

    @Test
    fun testGetPage() {
        given().accept(ContentType.JSON)
            .get("/1")
            .then()
            .statusCode(200)
    }


    @Test
    fun testAllPages() {
        val read = mutableSetOf<String>()

        var page = given().accept(ContentType.JSON)
            .get("/")
            .then()
            .statusCode(200)
            .body("data.list.size()", CoreMatchers.equalTo(page))
            .extract().body().jsonPath().getObject("data", object : TypeRef<PageDto<Map<String, Any>>>() {})
        read.addAll(page.list.map { it["id"].toString() })

        checkOrder(page)

        while (page.next != null) {
            page = given().accept(ContentType.JSON)
                .get(page.next)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject("data", object : TypeRef<PageDto<Map<String, Any>>>() {})
            read.addAll(page.list.map { it["id"].toString() })
            checkOrder(page)
        }
        assertEquals(repository.count().toInt(), read.size)
    }

    private fun checkOrder(page: PageDto<Map<String, Any>>) {
        for (i in 0 until page.list.size - 1) {
            val aid = page.list[i]["id"].toString()
            val bid = page.list[i + 1]["id"].toString()
            assertTrue(aid > bid)
        }
    }


    @Test
    fun testCreateMovie() {


        val pages = given().accept(ContentType.JSON)
            .get("/")
            .then()
            .statusCode(200)
            .body("data.list.size()", CoreMatchers.equalTo(page))
            .extract().body().jsonPath().getObject("data", object : TypeRef<PageDto<Map<String, Any>>>() {})

        val id: Int = pages.list.first()["id"].toString().toInt() + 1
        val movieDto = MovieDto().apply {
            this.id = id.toString(); title = "test"; director = "test"; year = "2021"; schedule = mutableListOf()
        }
        given().contentType(ContentType.JSON).body(movieDto)
            .put("/$id")
            .then()
            .statusCode(401)

        given().contentType(ContentType.JSON).body(movieDto)
            .auth().basic("bar", "123")
            .put("/$id")
            .then()
            .statusCode(403)

        given().contentType(ContentType.JSON).body(movieDto)
            .auth().basic("admin", "admin")
            .put("/$id")
            .then()
            .statusCode(201)
        page++

        given()
            .get("/$id")
            .then()
            .statusCode(200)

        given().accept(ContentType.JSON)
            .get("/")
            .then()
            .statusCode(200)
            .body("data.list.size()", CoreMatchers.equalTo(page))
    }


}