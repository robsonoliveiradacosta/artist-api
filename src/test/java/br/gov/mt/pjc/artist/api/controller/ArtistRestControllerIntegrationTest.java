package br.gov.mt.pjc.artist.api.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import br.gov.mt.pjc.artist.api.contract.request.ArtistRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ArtistRestControllerIntegrationTest {

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/v1/artists";
	}

	@Test
	public void shouldReturnStatus200_WhenFindAll() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void shouldReturnNameAndStatus200_WhenFindByIdExisting() {
		given()
			.pathParam("id", 1)
			.accept(ContentType.JSON)
		.when()
			.get("/{id}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("name", not(""));
	}

	@Test
	public void shouldReturnStatus404_WhenFindByIdNotExisting() {
		given()
			.pathParam("id", Integer.MAX_VALUE)
			.accept(ContentType.JSON)
		.when()
			.get("/{id}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void shouldReturnStatus200_WhenSearchArtistByName() {
		String name = "mi";

		given()
			.queryParam("name", name)
			.accept(ContentType.JSON)
		.when()
			.get("/search")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("", hasSize(2));
	}

	@Test
	public void shouldReturnStatus201_WhenCreateArtist() {
		ArtistRequest request = new ArtistRequest();
		request.setName("Korn");

		given()
			.body(request)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when().
			post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}

	@Test
	public void shouldReturnStatus200AndUpdateName_WhenUpdateArtist() {
		Long id = 1L;
		String artistName = "Java Dev";

		ArtistRequest request = new ArtistRequest();
		request.setName(artistName);

		given()
			.pathParam("id", id)
			.body(request)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when().
			put("/{id}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("name", equalTo(artistName));
	}

	@Test
	public void shouldReturnStatus204_WhenDeleteArtist() {
		long lastId = given().accept(ContentType.JSON)
			.when().get()
			.then().extract().jsonPath().getLong("id[-1]");

		given()
			.pathParam("id", lastId)
			.accept(ContentType.JSON)
		.when()
			.delete("/{id}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}

}
