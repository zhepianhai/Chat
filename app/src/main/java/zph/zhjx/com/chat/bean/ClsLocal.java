package zph.zhjx.com.chat.bean;

/**
 * Created by adminZPH on 2017/3/10.
 */

public class ClsLocal {

    /**
     * code : 0
     * data : {"state":1}
     * message : 状态更新成功，位置信息不可见
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
         * state : 1
         */

        private int state;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
