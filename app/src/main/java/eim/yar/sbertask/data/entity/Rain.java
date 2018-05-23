
package eim.yar.sbertask.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("PMD.DataClass")
public class Rain {

    @SerializedName("3h")
    @Expose
    private Double m3h;

    public Double get3h() {
        return m3h;
    }

    public void set3h(Double m3h) {
        this.m3h = m3h;
    }

}
