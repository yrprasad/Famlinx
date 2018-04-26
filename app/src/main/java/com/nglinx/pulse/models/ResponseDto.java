package com.nglinx.pulse.models;

import java.util.Set;

public class ResponseDto<M> {
	private boolean success = true;
	private String message;
	private M model;
	private Set<M> models;

	public ResponseDto() {
	}

	public ResponseDto(M model) {
		this.model = model;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set<M> getModels() {
		return models;
	}

	public void setModels(Set<M> models) {
		this.models = models;
	}

	public M getModel() {
		return model;
	}

	public void setModel(M model) {
		this.model = model;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
