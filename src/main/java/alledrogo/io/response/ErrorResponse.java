package alledrogo.io.response;

public class ErrorResponse {
    private String error;
    private String message;

    public ErrorResponse(String unauthorized, String message) {
        this.error = unauthorized;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
