package br.gov.mt.pjc.artist.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.gov.mt.pjc.artist.api.contract.request.ArtistRequest;
import br.gov.mt.pjc.artist.api.contract.response.ArtistResponse;
import br.gov.mt.pjc.artist.service.ArtistService;

@RestController
@RequestMapping("/v1/artists")
public class ArtistController {

	private final ArtistService service;

	public ArtistController(ArtistService service) {
		this.service = service;
	}

	@GetMapping
	public List<ArtistResponse> findAll() {
		return service.findAll();
	}

	@GetMapping(path = "/{id}")
	public ArtistResponse findById(@PathVariable Long id) {
		return service.findById(id);
	}

	@GetMapping(path = "/search")
	public List<ArtistResponse> findByName(@RequestParam(name = "name") String name,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		return service.findByName(name, Sort.Direction.fromString(direction));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ArtistResponse create(@RequestBody @Valid ArtistRequest request) {
		return service.create(request);
	}

	@PutMapping(path = "/{id}")
	public ArtistResponse update(@PathVariable Long id, @RequestBody @Valid ArtistRequest request) {
		return service.update(id, request);
	}

	@DeleteMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
}
