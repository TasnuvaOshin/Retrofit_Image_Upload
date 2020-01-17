package tasnuvaoshin.com.retrofitimageupload;

public class ResponseClass {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseClass() {
    }

    public ResponseClass(String message) {
        this.message = message;
    }
}
