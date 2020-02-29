package app.tuton.grocery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class bannerBean {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("banner")
    @Expose
    private String banner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }
}
