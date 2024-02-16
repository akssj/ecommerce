package alledrogo.io.response;

import java.io.Serializable;

//TODO get rid of it and make it so that JWtresponse is used to update data and renew token
/**
 * Response class for api to return data in object form instead of raw data.
 */
public class UserDataResponse implements Serializable {
    private String username;
    private String email;

    public UserDataResponse(String username, String email){
        this.username = username;
        this.email = email;
    }

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
}
