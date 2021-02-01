package br.gov.mt.pjc.artist.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.mt.pjc.artist.domain.model.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

}
