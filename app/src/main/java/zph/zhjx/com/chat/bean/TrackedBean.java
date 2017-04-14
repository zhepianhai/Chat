package zph.zhjx.com.chat.bean;

import java.util.List;

/**
 * Created by adminZPH on 2017/3/31.
 */

public class TrackedBean {

    /**
     * code : 0
     * data : {"list":[{"lat":"34.735738","lng":"113.613883","movement_time":1490251730000,"phone":"13103655893"},{"lat":"34.735756","lng":"113.613883","movement_time":1490251883000,"phone":"13103655893"},{"lat":"34.735737","lng":"113.613884","movement_time":1490251940000,"phone":"13103655893"},{"lat":"34.735762","lng":"113.613884","movement_time":1490252062000,"phone":"13103655893"},{"lat":"34.735764","lng":"113.613883","movement_time":1490252380000,"phone":"13103655893"},{"lat":"34.735759","lng":"113.613884","movement_time":1490252391000,"phone":"13103655893"},{"lat":"0.0","lng":"0.0","movement_time":1490252487000,"phone":"13103655893"},{"lat":"34.735738","lng":"113.613884","movement_time":1490252508000,"phone":"13103655893"},{"lat":"34.735765","lng":"113.613884","movement_time":1490252658000,"phone":"13103655893"},{"lat":"34.735742","lng":"113.613884","movement_time":1490252688000,"phone":"13103655893"},{"lat":"34.735762","lng":"113.613883","movement_time":1490252718000,"phone":"13103655893"},{"lat":"34.735741","lng":"113.613884","movement_time":1490252852000,"phone":"13103655893"},{"lat":"34.735767","lng":"113.613884","movement_time":1490253018000,"phone":"13103655893"},{"lat":"34.73574","lng":"113.613884","movement_time":1490253244000,"phone":"13103655893"},{"lat":"34.735325","lng":"113.614023","movement_time":1490253319000,"phone":"13103655893"},{"lat":"34.735744","lng":"113.613884","movement_time":1490253348000,"phone":"13103655893"},{"lat":"34.735765","lng":"113.613884","movement_time":1490253710000,"phone":"13103655893"},{"lat":"34.735739","lng":"113.613884","movement_time":1490253798000,"phone":"13103655893"},{"lat":"0.0","lng":"0.0","movement_time":1490253898000,"phone":"13103655893"},{"lat":"34.735713","lng":"113.613893","movement_time":1490780858000,"phone":"13103655893"},{"lat":"34.735713","lng":"113.613893","movement_time":1490780863000,"phone":"13103655893"},{"lat":"34.735767","lng":"113.613944","movement_time":1490780892000,"phone":"13103655893"},{"lat":"34.735738","lng":"113.613883","movement_time":1490251730000,"phone":"13103655893"},{"lat":"34.735756","lng":"113.613883","movement_time":1490251883000,"phone":"13103655893"},{"lat":"34.735737","lng":"113.613884","movement_time":1490251940000,"phone":"13103655893"},{"lat":"34.735762","lng":"113.613884","movement_time":1490252062000,"phone":"13103655893"},{"lat":"34.735764","lng":"113.613883","movement_time":1490252380000,"phone":"13103655893"},{"lat":"34.735759","lng":"113.613884","movement_time":1490252391000,"phone":"13103655893"},{"lat":"0.0","lng":"0.0","movement_time":1490252487000,"phone":"13103655893"},{"lat":"34.735738","lng":"113.613884","movement_time":1490252508000,"phone":"13103655893"},{"lat":"34.735765","lng":"113.613884","movement_time":1490252658000,"phone":"13103655893"},{"lat":"34.735742","lng":"113.613884","movement_time":1490252688000,"phone":"13103655893"},{"lat":"34.735762","lng":"113.613883","movement_time":1490252718000,"phone":"13103655893"},{"lat":"34.735741","lng":"113.613884","movement_time":1490252852000,"phone":"13103655893"},{"lat":"34.735767","lng":"113.613884","movement_time":1490253018000,"phone":"13103655893"},{"lat":"34.73574","lng":"113.613884","movement_time":1490253244000,"phone":"13103655893"},{"lat":"34.735325","lng":"113.614023","movement_time":1490253319000,"phone":"13103655893"},{"lat":"34.735744","lng":"113.613884","movement_time":1490253348000,"phone":"13103655893"},{"lat":"34.735765","lng":"113.613884","movement_time":1490253710000,"phone":"13103655893"},{"lat":"34.735739","lng":"113.613884","movement_time":1490253798000,"phone":"13103655893"},{"lat":"0.0","lng":"0.0","movement_time":1490253898000,"phone":"13103655893"},{"lat":"34.735738","lng":"113.613884","movement_time":1490253942000,"phone":"13103655893"},{"lat":"39.906782","lng":"116.397124","movement_time":1490348596000,"phone":"13103655893"},{"lat":"34.735713","lng":"113.613893","movement_time":1490780863000,"phone":"13103655893"},{"lat":"34.735767","lng":"113.613944","movement_time":1490780892000,"phone":"13103655893"}],"number":92}
     * message : 获取运动轨迹信息列表成功
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
         * list : [{"lat":"34.735738","lng":"113.613883","movement_time":1490251730000,"phone":"13103655893"},{"lat":"34.735756","lng":"113.613883","movement_time":1490251883000,"phone":"13103655893"},{"lat":"34.735737","lng":"113.613884","movement_time":1490251940000,"phone":"13103655893"},{"lat":"34.735762","lng":"113.613884","movement_time":1490252062000,"phone":"13103655893"},{"lat":"34.735764","lng":"113.613883","movement_time":1490252380000,"phone":"13103655893"},{"lat":"34.735759","lng":"113.613884","movement_time":1490252391000,"phone":"13103655893"},{"lat":"0.0","lng":"0.0","movement_time":1490252487000,"phone":"13103655893"},{"lat":"34.735738","lng":"113.613884","movement_time":1490252508000,"phone":"13103655893"},{"lat":"34.735765","lng":"113.613884","movement_time":1490252658000,"phone":"13103655893"},{"lat":"34.735742","lng":"113.613884","movement_time":1490252688000,"phone":"13103655893"},{"lat":"34.735762","lng":"113.613883","movement_time":1490252718000,"phone":"13103655893"},{"lat":"34.735741","lng":"113.613884","movement_time":1490252852000,"phone":"13103655893"},{"lat":"34.735767","lng":"113.613884","movement_time":1490253018000,"phone":"13103655893"},{"lat":"34.73574","lng":"113.613884","movement_time":1490253244000,"phone":"13103655893"},{"lat":"34.735325","lng":"113.614023","movement_time":1490253319000,"phone":"13103655893"},{"lat":"34.735744","lng":"113.613884","movement_time":1490253348000,"phone":"13103655893"},{"lat":"34.735765","lng":"113.613884","movement_time":1490253710000,"phone":"13103655893"},{"lat":"34.735739","lng":"113.613884","movement_time":1490253798000,"phone":"13103655893"},{"lat":"0.0","lng":"0.0","movement_time":1490253898000,"phone":"13103655893"},{"lat":"34.735713","lng":"113.613893","movement_time":1490780858000,"phone":"13103655893"},{"lat":"34.735713","lng":"113.613893","movement_time":1490780863000,"phone":"13103655893"},{"lat":"34.735767","lng":"113.613944","movement_time":1490780892000,"phone":"13103655893"},{"lat":"34.735738","lng":"113.613883","movement_time":1490251730000,"phone":"13103655893"},{"lat":"34.735756","lng":"113.613883","movement_time":1490251883000,"phone":"13103655893"},{"lat":"34.735737","lng":"113.613884","movement_time":1490251940000,"phone":"13103655893"},{"lat":"34.735762","lng":"113.613884","movement_time":1490252062000,"phone":"13103655893"},{"lat":"34.735764","lng":"113.613883","movement_time":1490252380000,"phone":"13103655893"},{"lat":"34.735759","lng":"113.613884","movement_time":1490252391000,"phone":"13103655893"},{"lat":"0.0","lng":"0.0","movement_time":1490252487000,"phone":"13103655893"},{"lat":"34.735738","lng":"113.613884","movement_time":1490252508000,"phone":"13103655893"},{"lat":"34.735765","lng":"113.613884","movement_time":1490252658000,"phone":"13103655893"},{"lat":"34.735742","lng":"113.613884","movement_time":1490252688000,"phone":"13103655893"},{"lat":"34.735762","lng":"113.613883","movement_time":1490252718000,"phone":"13103655893"},{"lat":"34.735741","lng":"113.613884","movement_time":1490252852000,"phone":"13103655893"},{"lat":"34.735767","lng":"113.613884","movement_time":1490253018000,"phone":"13103655893"},{"lat":"34.73574","lng":"113.613884","movement_time":1490253244000,"phone":"13103655893"},{"lat":"34.735325","lng":"113.614023","movement_time":1490253319000,"phone":"13103655893"},{"lat":"34.735744","lng":"113.613884","movement_time":1490253348000,"phone":"13103655893"},{"lat":"34.735765","lng":"113.613884","movement_time":1490253710000,"phone":"13103655893"},{"lat":"34.735739","lng":"113.613884","movement_time":1490253798000,"phone":"13103655893"},{"lat":"0.0","lng":"0.0","movement_time":1490253898000,"phone":"13103655893"},{"lat":"34.735738","lng":"113.613884","movement_time":1490253942000,"phone":"13103655893"},{"lat":"39.906782","lng":"116.397124","movement_time":1490348596000,"phone":"13103655893"},{"lat":"34.735713","lng":"113.613893","movement_time":1490780863000,"phone":"13103655893"},{"lat":"34.735767","lng":"113.613944","movement_time":1490780892000,"phone":"13103655893"}]
         * number : 92
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
             * lat : 34.735738
             * lng : 113.613883
             * movement_time : 1490251730000
             * phone : 13103655893
             */

            private String lat;
            private String lng;
            private long movement_time;
            private String phone;

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

            public long getMovement_time() {
                return movement_time;
            }

            public void setMovement_time(long movement_time) {
                this.movement_time = movement_time;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }
    }
}
