package zph.zhjx.com.chat.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adminZPH on 2017/3/8.
 * 数据库的用户层实体
 */
@Entity
public class User {
    private boolean login;//登录的状态
    @Id(autoincrement = false)
    private String phone;//用户的手机号
   /* @ToOne(joinProperty = "phone")
    private People id_phone;*/
    private String imageSrc;//用户头像地址
    private String token;//当前用户的token
    private String userName;//用户的名称
    private String userNickname;//用户的昵称
    private String address;//用户住址
    private long birthday;//用户生日
    private String gender;//性别
    private boolean position;
    private String imageBase64;
    @Generated(hash = 1279330190)
    public User(boolean login, String phone, String imageSrc, String token,
            String userName, String userNickname, String address, long birthday,
            String gender, boolean position, String imageBase64) {
        this.login = login;
        this.phone = phone;
        this.imageSrc = imageSrc;
        this.token = token;
        this.userName = userName;
        this.userNickname = userNickname;
        this.address = address;
        this.birthday = birthday;
        this.gender = gender;
        this.position = position;
        this.imageBase64 = imageBase64;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public boolean getLogin() {
        return this.login;
    }
    public void setLogin(boolean login) {
        this.login = login;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getImageSrc() {
        return this.imageSrc;
    }
    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserNickname() {
        return this.userNickname;
    }
    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public long getBirthday() {
        return this.birthday;
    }
    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public boolean getPosition() {
        return this.position;
    }
    public void setPosition(boolean position) {
        this.position = position;
    }
    public String getImageBase64() {
        return this.imageBase64;
    }
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

}
