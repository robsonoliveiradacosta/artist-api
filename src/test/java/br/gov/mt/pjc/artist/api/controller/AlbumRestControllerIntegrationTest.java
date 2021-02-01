package br.gov.mt.pjc.artist.api.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import br.gov.mt.pjc.artist.api.contract.request.AlbumRequest;
import br.gov.mt.pjc.artist.api.contract.request.ArtistIdRequest;
import br.gov.mt.pjc.artist.api.contract.request.ArtistRequest;
import br.gov.mt.pjc.artist.api.contract.response.AlbumResponse;
import br.gov.mt.pjc.artist.api.contract.response.ArtistResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AlbumRestControllerIntegrationTest {

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/v1/albums";
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
	public void shouldReturnStatus200_WhenSearchAlbumByArtistName() {
		String artistName = "serj tankian";

		given()
			.queryParam("artistName", artistName)
			.accept(ContentType.JSON)
		.when()
			.get("/search")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("", hasSize(3));
	}

	@Test
	public void shouldReturnStatus200_WhenPagedQuery() {
		Integer size = 5;
		Integer page = 1; // second page

		given()
			.queryParam("size", size)
			.queryParam("page", page)
			.accept(ContentType.JSON)
		.when()
			.get("/page")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("content", hasSize(size));
	}

	@Test
	public void shouldReturnStatus201_WhenCreateAlbum() {
		ArtistRequest artistRequest = new ArtistRequest();
		artistRequest.setName("Korn");

		ArtistResponse artistResponse = given()
			.basePath("/v1/artists")
			.body(artistRequest)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when().
			post()
		.then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().jsonPath().getObject("", ArtistResponse.class);

		ArtistIdRequest artistIdRequest = new ArtistIdRequest();
		artistIdRequest.setId(artistResponse.getId());
		AlbumRequest albumRequest = new AlbumRequest();
		albumRequest.setName("Untouchables");
		albumRequest.setArtist(artistIdRequest);

		given()
			.body(albumRequest)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when().
			post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}

	@Test
	public void shouldReturnStatus200AndUpdateName_WhenUpdateAlbum() {
		Long id = 1L;
		Long artistId = 1L;
		String albumName = "Java Album";

		AlbumRequest request = new AlbumRequest();
		request.setName(albumName);
		ArtistIdRequest artistIdRequest = new ArtistIdRequest();
		artistIdRequest.setId(artistId);
		request.setArtist(artistIdRequest);

		given()
			.pathParam("id", id)
			.body(request)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when().
			put("/{id}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("name", equalTo(albumName));
	}

	@Test
	public void shouldReturnStatus204_WhenDeleteAlbum() {
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

	@Test
	public void shouldReturnStatus200_WhenAddCoverToAlbum() {
		Long id = 1L;

		given()
			.pathParam("id", id)
			.multiPart("file", new File("src/test/resources/nevermind.jpeg"))
		.when().
			put("/{id}/covers")
		.then()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void shouldReturnStatus200_WhenDeleteCoverFromAlbum() {
		Long id = 1L;

		given()
			.pathParam("id", id)
			.multiPart("file", new File("src/test/resources/nevermind.jpeg"))
		.when().
			put("/{id}/covers")
		.then()
			.statusCode(HttpStatus.OK.value());

		AlbumResponse albumResponse =
			given().pathParam("id", id).accept(ContentType.JSON)
			.when().get("/{id}")
			.then().extract().jsonPath().getObject("", AlbumResponse.class);

		String objectName = albumResponse.getCovers().get(0).getObjectName();

		given()
			.pathParam("objectName", objectName)
		.when()
			.delete("/covers/{objectName}")
		.then()
			.statusCode(HttpStatus.OK.value());
	}

}
