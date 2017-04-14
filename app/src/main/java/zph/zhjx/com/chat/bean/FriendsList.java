package zph.zhjx.com.chat.bean;

import java.util.List;

/**
 * Created by adminZPH on 2017/3/14.
 */

public class FriendsList {

    /**
     * code : 0
     * data : {"list":[{"nickName":"阿三","phone":"12345678901","userId":1},{"nickName":"002","phone":"12345678902","userId":2},{"nickName":"小五","phone":"123456","userId":3},{"nickName":"小六","phone":"12345678","userId":4},{"nickName":"333","phone":"12345678903","userId":5},{"nickName":"小四","phone":"12345678904","userId":6},{"nickName":"7","phone":"12345678905","userId":7},{"nickName":"8","phone":"12345678906","userId":8},{"nickName":"9","phone":"12345678907","userId":9},{"nickName":"11","phone":"12345678911","userId":10},{"nickName":"12","phone":"12345678912","userId":11},{"nickName":"13","phone":"12345678913","userId":12},{"nickName":"14","phone":"12345678914","userId":13},{"nickName":"15","phone":"12345678915","userId":14},{"nickName":"16","phone":"12345678916","userId":15},{"nickName":"17","phone":"12345678917","userId":16},{"nickName":"18","phone":"12345678918","userId":17},{"nickName":"19","phone":"12345678919","userId":18},{"nickName":"半妖","phone":"12345678921","photo":"qwe","userId":25}],"number":19}
     * message : 获取好友列表成功
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

    @Override
    public String toString() {
        return "Friends{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * list : [{"nickName":"阿三","phone":"12345678901","userId":1},{"nickName":"002","phone":"12345678902","userId":2},{"nickName":"小五","phone":"123456","userId":3},{"nickName":"小六","phone":"12345678","userId":4},{"nickName":"333","phone":"12345678903","userId":5},{"nickName":"小四","phone":"12345678904","userId":6},{"nickName":"7","phone":"12345678905","userId":7},{"nickName":"8","phone":"12345678906","userId":8},{"nickName":"9","phone":"12345678907","userId":9},{"nickName":"11","phone":"12345678911","userId":10},{"nickName":"12","phone":"12345678912","userId":11},{"nickName":"13","phone":"12345678913","userId":12},{"nickName":"14","phone":"12345678914","userId":13},{"nickName":"15","phone":"12345678915","userId":14},{"nickName":"16","phone":"12345678916","userId":15},{"nickName":"17","phone":"12345678917","userId":16},{"nickName":"18","phone":"12345678918","userId":17},{"nickName":"19","phone":"12345678919","userId":18},{"nickName":"半妖","phone":"12345678921","photo":"qwe","userId":25}]
         * number : 19
         */

        private int number;
        private List<ListBean> list;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            @Override
            public String toString() {
                return "ListBean{" +
                        "nickName='" + nickName + '\'' +
                        ", phone='" + phone + '\'' +
                        ", userId=" + userId +
                        ", photo='" + photo + '\'' +
                        '}';
            }

            /**
             * nickName : 阿三
             * phone : 12345678901
             * userId : 1
             * photo : qwe
             */

            private String nickName;
            private String phone;
            private int userId;
            private String photo;

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }
        }
    }
}
