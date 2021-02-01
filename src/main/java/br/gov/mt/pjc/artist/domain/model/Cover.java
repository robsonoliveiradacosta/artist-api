package br.gov.mt.pjc.artist.domain.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Cover {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private UUID objectName;

	@ManyToOne
	private Album album;

	public Cover() {
	}

	public Cover(Long albumId, String objectName) {
		this.album = new Album();
		this.album.setId(albumId);
		this.objectName = UUID.fromString(objectName);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UUID getObjectName() {
		return objectName;
	}

	public void setObjectName(UUID objectName) {
		this.objectName = objectName;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cover other = (Cover) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
