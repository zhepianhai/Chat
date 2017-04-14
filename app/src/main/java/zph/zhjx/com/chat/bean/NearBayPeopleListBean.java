package zph.zhjx.com.chat.bean;

import java.util.List;

/**
 * Created by adminZPH on 2017/3/13.
 */

public class NearBayPeopleListBean {


    /**
     * code : 0
     * data : {"list":[{"distance":"0米","gender":"m","lat":345,"lng":456,"name":"蓝","nickName":"Fls","phone":"12345678919","photo":"http://www.feizl.com/upload2007/2011_10/111013165656687.jpg"},{"distance":"0米","gender":"f","lat":345,"lng":456,"name":"范冰冰","nickName":"冰冰","phone":"12345678","photo":"http://img3.imgtn.bdimg.com/it/u=1968895871,2132626536&fm=214&gp=0.jpg"},{"distance":"0米","gender":"f","lat":345,"lng":456,"name":"范冰冰","nickName":"冰冰","phone":"12345678","photo":"http://img3.imgtn.bdimg.com/it/u=1968895871,2132626536&fm=214&gp=0.jpg"},{"distance":"0米","gender":"f","lat":345,"lng":456,"name":"范冰冰","nickName":"冰冰","phone":"12345678","photo":"http://img3.imgtn.bdimg.com/it/u=1968895871,2132626536&fm=214&gp=0.jpg"},{"distance":"133米","gender":"f","lat":345.0012,"lng":456,"name":"泰勒·斯威夫特","nickName":"Taylor Swift","phone":"12345678914","photo":"https://imgsa.baidu.com/baike/c0%3Dbaike180%2C5%2C5%2C180%2C60/sign=1cb22fbd9413b07ea9b0585a6dbefa46/e4dde71190ef76c640af53b79a16fdfaaf516729.jpg"},{"distance":"144米","gender":"m","lat":345.0013,"lng":456,"name":"道恩·强森","nickName":"Dwayne Johnson","phone":"12345678915","photo":"https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1489543998&di=a229d756134726e691d94fadc9e12891&src=http://photocdn.sohu.com/20150424/Img411807143.jpg"},{"distance":"334米","gender":"m","lat":345.003,"lng":456,"name":"刘德华","nickName":"华仔","phone":"12345678911","photo":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489553852154&di=24601de153e4c476e8de2e847def41ff&imgtype=0&src=http%3A%2F%2Fwww.qxjlm.com%2Ftupians%2Fbd14859570.jpg"},{"distance":"1113米","gender":"f","lat":345.01,"lng":456,"name":"天","nickName":"Sky","phone":"12345678918","photo":"http://i1.sanwen8.cn/doc/1609/676-160ZG45922.jpg"}],"number":8}
     * message : 获取附近用户信息成功
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
         * list : [{"distance":"0米","gender":"m","lat":345,"lng":456,"name":"蓝","nickName":"Fls","phone":"12345678919","photo":"http://www.feizl.com/upload2007/2011_10/111013165656687.jpg"},{"distance":"0米","gender":"f","lat":345,"lng":456,"name":"范冰冰","nickName":"冰冰","phone":"12345678","photo":"http://img3.imgtn.bdimg.com/it/u=1968895871,2132626536&fm=214&gp=0.jpg"},{"distance":"0米","gender":"f","lat":345,"lng":456,"name":"范冰冰","nickName":"冰冰","phone":"12345678","photo":"http://img3.imgtn.bdimg.com/it/u=1968895871,2132626536&fm=214&gp=0.jpg"},{"distance":"0米","gender":"f","lat":345,"lng":456,"name":"范冰冰","nickName":"冰冰","phone":"12345678","photo":"http://img3.imgtn.bdimg.com/it/u=1968895871,2132626536&fm=214&gp=0.jpg"},{"distance":"133米","gender":"f","lat":345.0012,"lng":456,"name":"泰勒·斯威夫特","nickName":"Taylor Swift","phone":"12345678914","photo":"https://imgsa.baidu.com/baike/c0%3Dbaike180%2C5%2C5%2C180%2C60/sign=1cb22fbd9413b07ea9b0585a6dbefa46/e4dde71190ef76c640af53b79a16fdfaaf516729.jpg"},{"distance":"144米","gender":"m","lat":345.0013,"lng":456,"name":"道恩·强森","nickName":"Dwayne Johnson","phone":"12345678915","photo":"https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1489543998&di=a229d756134726e691d94fadc9e12891&src=http://photocdn.sohu.com/20150424/Img411807143.jpg"},{"distance":"334米","gender":"m","lat":345.003,"lng":456,"name":"刘德华","nickName":"华仔","phone":"12345678911","photo":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489553852154&di=24601de153e4c476e8de2e847def41ff&imgtype=0&src=http%3A%2F%2Fwww.qxjlm.com%2Ftupians%2Fbd14859570.jpg"},{"distance":"1113米","gender":"f","lat":345.01,"lng":456,"name":"天","nickName":"Sky","phone":"12345678918","photo":"http://i1.sanwen8.cn/doc/1609/676-160ZG45922.jpg"}]
         * number : 8
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
            /**
             * distance : 0米
             * gender : m
             * lat : 345
             * lng : 456
             * name : 蓝
             * nickName : Fls
             * phone : 12345678919
             * photo : http://www.feizl.com/upload2007/2011_10/111013165656687.jpg
             */

            private String distance;
            private String gender;
            private String lat;
            private String lng;
            private String name;
            private String nickName;
            private String phone;
            private String photo;

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }
        }
    }
}
