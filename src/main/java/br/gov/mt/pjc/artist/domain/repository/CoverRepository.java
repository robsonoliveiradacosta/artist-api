package br.gov.mt.pjc.artist.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.mt.pjc.artist.domain.model.Cover;

@Repository
public interface CoverRepository extends JpaRepository<Cover, Long> {

	Optional<Cover> findByObjectName(UUID objectName);

}
