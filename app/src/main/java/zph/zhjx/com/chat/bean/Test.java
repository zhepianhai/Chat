package zph.zhjx.com.chat.bean;

import java.util.List;

/**
 * Created by adminZPH on 2017/3/6.
 */

public class Test {

    private List<HahaBean> haha;

    public List<HahaBean> getHaha() {
        return haha;
    }

    public void setHaha(List<HahaBean> haha) {
        this.haha = haha;
    }

    public static class HahaBean {
        @Override
        public String toString() {
            return "HahaBean{" +
                    "position='" + position + '\'' +
                    ", area='" + area + '\'' +
                    ", circulation='" + circulation + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }

        /**
         * position : .。。。。。。。。。。。。
         * area : 300
         * circulation : 合作
         * type : 水地
         */

        private String position;
        private String area;
        private String circulation;
        private String type;

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCirculation() {
            return circulation;
        }

        public void setCirculation(String circulation) {
            this.circulation = circulation;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    @Override
    public String toString() {
        return "Test{" +
                "haha=" + haha +
                '}';
    }
}
