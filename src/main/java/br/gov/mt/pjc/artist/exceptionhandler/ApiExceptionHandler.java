package br.gov.mt.pjc.artist.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.gov.mt.pjc.artist.exception.MinioUploadException;
import br.gov.mt.pjc.artist.exception.ResourceNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@Value("${spring.servlet.multipart.max-file-size}")
	private String maxFileSize;

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String message = "O corpo da requisição está inválido.";
		ApiError apiError = new ApiError(status.value(), message, OffsetDateTime.now());
		return new ResponseEntity<>(apiError, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<ApiError.Field> fields = ex.getBindingResult().getAllErrors().stream().map(objectError -> {
			String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
			String name = ((FieldError) objectError).getField();
			return new ApiError.Field(name, message);
		}).collect(Collectors.toList());
		String message = "Um ou mais campos estão inválidos.";
		ApiError apiError = new ApiError(status.value(), message, OffsetDateTime.now(), fields);
		return new ResponseEntity<>(apiError, headers, status);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	private ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage(), OffsetDateTime.now());
		return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	private ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ApiError apiError = new ApiError(status.value(), ex.getMessage(), OffsetDateTime.now());
		return new ResponseEntity<>(apiError, status);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	private ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
			WebRequest request) {
		HttpStatus status = HttpStatus.CONFLICT;
		ApiError apiError = new ApiError(status.value(), "Operação não permitida", OffsetDateTime.now());
		return new ResponseEntity<>(apiError, status);
	}

	@ExceptionHandler(MinioUploadException.class)
	private ResponseEntity<ApiError> handleMinioUploadException(MinioUploadException ex, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), OffsetDateTime.now());
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	private ResponseEntity<ApiError> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex,
			WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(),
				String.format("Tamanho máximo do arquivo: %s", maxFileSize), OffsetDateTime.now());
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

}
