package br.gov.mt.pjc.artist.api.contract.request;

import javax.validation.constraints.NotBlank;

public class ArtistRequest {

	@NotBlank
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
