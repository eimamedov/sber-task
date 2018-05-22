
package eim.yar.sbertask.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("PMD.DataClass")
public class Rain {

    @SerializedName("3h")
    @Expose
    private int m3h;

    public int get3h() {
        return m3h;
    }

    public void set3h(int m3h) {
        this.m3h = m3h;
    }

}
