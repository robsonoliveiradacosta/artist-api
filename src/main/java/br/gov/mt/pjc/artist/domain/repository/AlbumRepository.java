package br.gov.mt.pjc.artist.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.mt.pjc.artist.domain.model.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

	boolean existsByName(String name);

	boolean existsByNameAndIdNot(String name, Long id);

	List<Album> findByArtistNameContainingIgnoreCase(String artistName);

}
