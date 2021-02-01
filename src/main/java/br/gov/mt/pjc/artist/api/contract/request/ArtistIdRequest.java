package br.gov.mt.pjc.artist.api.contract.request;

import javax.validation.constraints.NotNull;

public class ArtistIdRequest {

	@NotNull
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
