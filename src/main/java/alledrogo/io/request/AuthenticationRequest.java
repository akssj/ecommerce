package alledrogo.io.request;

import javax.validation.constraints.NotBlank;

/**
 * Request class, provides object instead of raw data for api params
 */
public class AuthenticationRequest {
	@NotBlank
	private String username;
	@NotBlank
	private String password;

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
}
