package zph.zhjx.com.chat.bean;

/**
 * Created by adminZPH on 2017/3/14.
 */

public class DetailpeopleBean {

    /**
     * code : 0
     * data : {"address":"郑州市中原区","birthday":1489469224000,"gender":"f","phone":"12345678901","photo":"/images/Aatrox.png","userName":"暗裔剑魔","userNickname":"亚托克斯"}
     * message : 获取该好友详细信息成功
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
        @Override
        public String toString() {
            return "DataBean{" +
                    "address='" + address + '\'' +
                    ", birthday=" + birthday +
                    ", gender='" + gender + '\'' +
                    ", phone='" + phone + '\'' +
                    ", photo='" + photo + '\'' +
                    ", userName='" + userName + '\'' +
                    ", userNickname='" + userNickname + '\'' +
                    '}';
        }

        /**
         * address : 郑州市中原区
         * birthday : 1489469224000
         * gender : f
         * phone : 12345678901
         * photo : /images/Aatrox.png
         * userName : 暗裔剑魔
         * userNickname : 亚托克斯
         */

        private String address;
        private long birthday;
        private String gender;
        private String phone;
        private String photo;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
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
