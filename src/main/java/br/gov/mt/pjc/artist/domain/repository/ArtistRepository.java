package br.gov.mt.pjc.artist.domain.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.mt.pjc.artist.domain.model.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

	boolean existsByName(String name);

	boolean existsByNameAndIdNot(String name, Long id);

	List<Artist> findByNameContainingIgnoreCase(String name, Sort sort);

}
