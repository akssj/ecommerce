package alledrogo.io.request;

import javax.validation.constraints.NotBlank;

/**
 * Request class, provides object data for api params
 */
public class AuthenticationRequest {
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private String newPassword;
	@NotBlank
	private String confirmPassword;
	@NotBlank
	private String email;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
