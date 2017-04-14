package zph.zhjx.com.chat.bean;

/**
 * Created by adminZPH on 2017/3/10.
 */

public class LogoutBeans {

    /**
     * code : 0
     * data : {"normal":0}
     * message : 退出成功
     */

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * normal : 0
         */

        private int normal;

        public int getNormal() {
            return normal;
        }

        public void setNormal(int normal) {
            this.normal = normal;
        }
    }
}
