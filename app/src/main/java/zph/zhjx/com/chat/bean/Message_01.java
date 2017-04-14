package zph.zhjx.com.chat.bean;

/**
 * Created by adminZPH on 2017/3/17.
 */

public class Message_01 {
    private String message;
    private int code;

    @Override
    public String toString() {
        return "Message_01{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
