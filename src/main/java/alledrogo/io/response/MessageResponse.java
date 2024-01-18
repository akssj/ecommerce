package alledrogo.io.response;

import java.io.Serializable;

/**
 * Response class for api to return data in object form instead of raw data.
 */
public class MessageResponse implements Serializable {
  private String message;

  public MessageResponse(String message) {
    this.message = message;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }

}
