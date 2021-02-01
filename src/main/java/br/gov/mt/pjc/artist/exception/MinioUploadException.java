package br.gov.mt.pjc.artist.exception;

public class MinioUploadException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MinioUploadException(String message) {
		super(message);
	}

}
