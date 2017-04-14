package zph.zhjx.com.chat.bean;

import java.io.Serializable;

/**
 * Created by adminZPH on 2017/3/28.
 */

public class SaleAttribute implements Serializable{
    private String value;
    private String id;
    private boolean isChecked;
    public SaleAttribute(){}
    public SaleAttribute(String value,String id,boolean isChecked){
        this.id=id;
        this.value=value;
        this.isChecked=isChecked;

    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    @Override
    public String toString() {
        return "SaleAttribute{" +
                "value='" + value + '\'' +
                ", id='" + id + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
