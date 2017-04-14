package zph.zhjx.com.chat.bean;

import java.io.Serializable;

/**
 * Created by adminZPH on 2017/3/3.
 */

public class User implements Serializable{

    /**
     * code : 0
     * data : {"address":"郑州市金水区","birthday":1489543418000,"gender":"f","normal":1,"photo":"http://img3.imgtn.bdimg.com/it/u=1968895871,2132626536&fm=214&gp=0.jpg","token":"af4ee50a-6470-4193-b7ce-7f7f773e64c1","userId":4,"userName":"范冰冰","userNickname":"冰冰"}
     * message : 登陆成功
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
         * address : 郑州市金水区
         * birthday : 1489543418000
         * gender : f
         * normal : 1
         * photo : http://img3.imgtn.bdimg.com/it/u=1968895871,2132626536&fm=214&gp=0.jpg
         * token : af4ee50a-6470-4193-b7ce-7f7f773e64c1
         * userId : 4
         * userName : 范冰冰
         * userNickname : 冰冰
         */

        private String address;
        private long birthday;
        private String gender;
        private int normal;
        private String photo;
        private String token;
        private int userId;
        private String userName;
        private String userNickname;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public long getBirthday() {
            return birthday;
        }

        public void setBirthday(long birthday) {
            this.birthday = birthday;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getNormal() {
            return normal;
        }

        public void setNormal(int normal) {
            this.normal = normal;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserNickname() {
            return userNickname;
        }

        public void setUserNickname(String userNickname) {
            this.userNickname = userNickname;
        }
    }
}
