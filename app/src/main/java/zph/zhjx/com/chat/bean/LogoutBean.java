package zph.zhjx.com.chat.bean;

/**
 * Created by adminZPH on 2017/3/8.
 */

public class LogoutBean {

    /**
     * code : 0
     * message : 登陆或登出日志记录成功
     */

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
