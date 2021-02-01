package br.gov.mt.pjc.artist.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.gov.mt.pjc.artist.api.contract.request.ArtistRequest;
import br.gov.mt.pjc.artist.api.contract.response.ArtistResponse;
import br.gov.mt.pjc.artist.domain.model.Artist;
import br.gov.mt.pjc.artist.domain.repository.ArtistRepository;
import br.gov.mt.pjc.artist.exception.ResourceNotFoundException;

@Service
public class ArtistService {

	private final ArtistRepository repository;
	private final ModelMapper modelMapper;

	public ArtistService(ArtistRepository repository, ModelMapper modelMapper) {
		this.repository = repository;
		this.modelMapper = modelMapper;
	}

	public List<ArtistResponse> findAll() {
		return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
	}

	public ArtistResponse findById(Long id) {
		Artist artist = findBy(id);
		return toResponse(artist);
	}

	public List<ArtistResponse> findByName(String name, Sort.Direction direction) {
		return repository.findByNameContainingIgnoreCase(name, Sort.by(direction, "name")).stream()
				.map(this::toResponse).collect(Collectors.toList());
	}

	@Transactional
	public ArtistResponse create(ArtistRequest request) {
		Artist artist = modelMapper.map(request, Artist.class);
		if (repository.existsByName(request.getName())) {
			throw new IllegalArgumentException("Nome já existente!");
		}
		return toResponse(repository.save(artist));
	}

	@Transactional
	public ArtistResponse update(Long id, ArtistRequest request) {
		Artist artist = findBy(id);
		modelMapper.map(request, artist);
		if (repository.existsByNameAndIdNot(request.getName(), artist.getId())) {
			throw new IllegalArgumentException("Nome já existente!");
		}
		return toResponse(repository.save(artist));
	}

	@Transactional
	public void delete(Long id) {
		Artist artist = findBy(id);
		repository.delete(artist);
	}

	private Artist findBy(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
	}

	private ArtistResponse toResponse(Artist artist) {
		return modelMapper.map(artist, ArtistResponse.class);
	}

}
