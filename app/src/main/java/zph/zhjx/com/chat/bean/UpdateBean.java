package zph.zhjx.com.chat.bean;

/**
 * Created by adminZPH on 2017/4/6.
 */

public class UpdateBean {

    /**
     * version : 1.1.0
     * name : gridapp_apk_1.1.0
     * url : http://zhjxdns.imwork.net/8088/gridapp/gridapp_apk_1.1.0.apk
     * content : 查看联系人、附近的人、网格留言、运动轨迹功能。
     */

    private String version;
    private String name;
    private String url;
    private String content;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "UpdateBean{" +
                "version='" + version + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
