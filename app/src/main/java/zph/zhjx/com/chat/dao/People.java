package zph.zhjx.com.chat.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adminZPH on 2017/3/14.
 * 数据库的用户层实体 联系人
 */
@Entity
public class People {
    @Id(autoincrement = true)
    private Long id;
    private String id_phone;//phone表示是手机号，是当前用户登录的手机作为ＩＤ
    private String user_phone;//当前用户的一个联系人的手机号信息
    private String name;//用户的真实名称
    private String nickname;//用户的昵称
    private String UID;//用户的在该联系中的ID
    private String image;//用户的头像信息
    private String pinyinname;//拼音名字
    private String beizhu;//用户的备注
    private String address;//用户地址
    private String birthday;//生日
    private String gender;//性别
    private String bitmap_base64;//用户默认缓存图片
    @Generated(hash = 617756555)
    public People(Long id, String id_phone, String user_phone, String name,
            String nickname, String UID, String image, String pinyinname,
            String beizhu, String address, String birthday, String gender,
            String bitmap_base64) {
        this.id = id;
        this.id_phone = id_phone;
        this.user_phone = user_phone;
        this.name = name;
        this.nickname = nickname;
        this.UID = UID;
        this.image = image;
        this.pinyinname = pinyinname;
        this.beizhu = beizhu;
        this.address = address;
        this.birthday = birthday;
        this.gender = gender;
        this.bitmap_base64 = bitmap_base64;
    }
    @Generated(hash = 1406030881)
    public People() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getId_phone() {
        return this.id_phone;
    }
    public void setId_phone(String id_phone) {
        this.id_phone = id_phone;
    }
    public String getUser_phone() {
        return this.user_phone;
    }
    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getUID() {
        return this.UID;
    }
    public void setUID(String UID) {
        this.UID = UID;
    }
    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getPinyinname() {
        return this.pinyinname;
    }
    public void setPinyinname(String pinyinname) {
        this.pinyinname = pinyinname;
    }
    public String getBeizhu() {
        return this.beizhu;
    }
    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getBirthday() {
        return this.birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getBitmap_base64() {
        return this.bitmap_base64;
    }
    public void setBitmap_base64(String bitmap_base64) {
        this.bitmap_base64 = bitmap_base64;
    }

}
