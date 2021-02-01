package br.gov.mt.pjc.artist.api.contract.request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AlbumRequest {

	@NotBlank
	private String name;

	@Valid
	@NotNull
	private ArtistIdRequest artist;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArtistIdRequest getArtist() {
		return artist;
	}

	public void setArtist(ArtistIdRequest artist) {
		this.artist = artist;
	}

}
