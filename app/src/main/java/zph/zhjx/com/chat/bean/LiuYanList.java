package zph.zhjx.com.chat.bean;

import java.util.List;

/**
 * Created by adminZPH on 2017/3/20.
 * 网格留言查询的实体类
 */

public class LiuYanList {

    @Override
    public String toString() {
        return "LiuYanList{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }

    /**
     * code : 0
     * data : {"list":[{"content":"","lat":"","lng":"","message_id":"","nickName":"","phone":"","time":""}],"number":0}
     * message : 获取附近用户留言信息成功
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
                    "number=" + number +
                    ", list=" + list +
                    '}';
        }

        /**
         * list : [{"content":"","lat":"","lng":"","message_id":"","nickName":"","phone":"","time":""}]
         * number : 0
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
                        "content='" + content + '\'' +
                        ", lat='" + lat + '\'' +
                        ", lng='" + lng + '\'' +
                        ", message_id='" + message_id + '\'' +
                        ", nickName='" + nickName + '\'' +
                        ", phone='" + phone + '\'' +
                        ", time='" + time + '\'' +
                        '}';
            }

            /**
             * content :
             * lat :
             * lng :
             * message_id :
             * nickName :
             * phone :
             * time :
             */

            private String content;
            private String lat;
            private String lng;
            private String message_id;
            private String nickName;
            private String phone;
            private String time;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }

            public String getMessage_id() {
                return message_id;
            }

            public void setMessage_id(String message_id) {
                this.message_id = message_id;
            }

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

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
