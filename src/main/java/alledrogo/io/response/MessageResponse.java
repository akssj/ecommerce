package alledrogo.io.response;

import java.io.Serializable;

/**
 * Response class for api to return data in object form instead of raw data.
 */
public class MessageResponse implements Serializable {
  private String message;
  private Long id;
  public MessageResponse(String message) {
    this.message = message;
  }
  public MessageResponse(String message, Long id) {
    this.message = message;
    this.id = id;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
