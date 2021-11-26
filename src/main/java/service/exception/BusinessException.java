package service.exception;

public class BusinessException extends RuntimeException{
    private String code;  //exception id
    private String message; //exception message

    public BusinessException(String code, String message) {
        super(code+":"+message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
