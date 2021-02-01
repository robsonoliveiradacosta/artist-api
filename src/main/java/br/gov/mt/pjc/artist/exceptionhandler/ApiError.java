package br.gov.mt.pjc.artist.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ApiError {

	private Integer status;
	private String message;
	private OffsetDateTime timestamp;
	private List<Field> fields;

	public ApiError(Integer status, String message, OffsetDateTime timestamp) {
		this.status = status;
		this.message = message;
		this.timestamp = timestamp;
	}

	public ApiError(Integer status, String message, OffsetDateTime timestamp, List<Field> fields) {
		this(status, message, timestamp);
		this.fields = fields;
	}

	public Integer getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public OffsetDateTime getTimestamp() {
		return timestamp;
	}

	public List<Field> getFields() {
		return fields;
	}

	public static class Field {
		private String name;
		private String message;

		public Field(String name, String message) {
			this.name = name;
			this.message = message;
		}

		public String getName() {
			return name;
		}

		public String getMessage() {
			return message;
		}
	}

}
