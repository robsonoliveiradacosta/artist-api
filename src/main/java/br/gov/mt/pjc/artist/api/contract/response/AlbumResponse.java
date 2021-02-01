package br.gov.mt.pjc.artist.api.contract.response;

import java.util.List;

public class AlbumResponse {

	private Long id;
	private String name;
	private ArtistResponse artist;
	private List<CoverResponse> covers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArtistResponse getArtist() {
		return artist;
	}

	public void setArtist(ArtistResponse artist) {
		this.artist = artist;
	}

	public List<CoverResponse> getCovers() {
		return covers;
	}

	public void setCovers(List<CoverResponse> covers) {
		this.covers = covers;
	}

}
