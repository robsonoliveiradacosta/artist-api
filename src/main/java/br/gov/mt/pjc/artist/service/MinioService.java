package br.gov.mt.pjc.artist.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.gov.mt.pjc.artist.config.MinioProperties;
import br.gov.mt.pjc.artist.exception.MinioUploadException;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;

@Service
public class MinioService {

	private final MinioClient minioClient;
	private final MinioProperties minioProperties;

	public MinioService(MinioClient minioClient, MinioProperties minioProperties) {
		this.minioClient = minioClient;
		this.minioProperties = minioProperties;
	}

	public String upload(MultipartFile file) {
		if (file.isEmpty()) {
			throw new MinioUploadException("informe o arquivo");
		}
		try {
			boolean found = minioClient
					.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucket()).build());
			if (!found) {
				minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucket()).build());
			}

			String objectName = UUID.randomUUID().toString();
			minioClient.putObject(PutObjectArgs.builder().bucket(minioProperties.getBucket()).object(objectName)
					.stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build());
			return objectName;

		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
				| InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
				| IllegalArgumentException | IOException e) {
			throw new MinioUploadException(e.getMessage());
		}
	}

	public void remove(String objectName) {
		try {
			minioClient.removeObject(
					RemoveObjectArgs.builder().bucket(minioProperties.getBucket()).object(objectName).build());
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
				| InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
				| IllegalArgumentException | IOException e) {
			throw new MinioUploadException(e.getMessage());
		}
	}

	public String getUrl(String objectName) {
		try {
			return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET)
					.bucket(minioProperties.getBucket()).object(objectName).expiry(3, TimeUnit.HOURS).build());
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
				| InvalidResponseException | NoSuchAlgorithmException | XmlParserException | ServerException
				| IllegalArgumentException | IOException e) {
			return "";
		}
	}

}
