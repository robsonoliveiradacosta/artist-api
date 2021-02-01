package br.gov.mt.pjc.artist.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.gov.mt.pjc.artist.api.contract.request.AlbumRequest;
import br.gov.mt.pjc.artist.api.contract.response.AlbumResponse;
import br.gov.mt.pjc.artist.domain.model.Album;
import br.gov.mt.pjc.artist.domain.repository.AlbumRepository;
import br.gov.mt.pjc.artist.exception.ResourceNotFoundException;

@Service
public class AlbumService {

	private final AlbumRepository repository;
	private final ModelMapper modelMapper;

	public AlbumService(AlbumRepository repository, ModelMapper modelMapper) {
		this.repository = repository;
		this.modelMapper = modelMapper;
	}

	public List<AlbumResponse> findAll() {
		return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
	}

	public Page<AlbumResponse> findAll(Pageable pageable) {
		return repository.findAll(pageable).map(this::toResponse);
	}

	public AlbumResponse findById(Long id) {
		Album album = findBy(id);
		return toResponse(album);
	}

	public List<AlbumResponse> findByArtistName(String artistName) {
		return repository.findByArtistNameContainingIgnoreCase(artistName).stream().map(this::toResponse)
				.collect(Collectors.toList());
	}

	@Transactional
	public AlbumResponse create(AlbumRequest request) {
		Album album = modelMapper.map(request, Album.class);
		if (repository.existsByName(request.getName())) {
			throw new IllegalArgumentException("Nome já existente!");
		}
		return toResponse(repository.save(album));
	}

	@Transactional
	public AlbumResponse update(Long id, AlbumRequest request) {
		Album album = findBy(id);
		modelMapper.map(request, album);
		if (repository.existsByNameAndIdNot(request.getName(), album.getId())) {
			throw new IllegalArgumentException("Nome já existente!");
		}
		return toResponse(repository.save(album));
	}

	@Transactional
	public void delete(Long id) {
		Album album = findBy(id);
		repository.delete(album);
	}

	private Album findBy(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
	}

	private AlbumResponse toResponse(Album album) {
		return modelMapper.map(album, AlbumResponse.class);
	}

}
