package main.dto.request;

import javax.validation.constraints.NotBlank;

public class UserStatusRequest {
	@NotBlank
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
