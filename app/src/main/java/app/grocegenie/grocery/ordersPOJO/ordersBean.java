package app.grocegenie.grocery.ordersPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ordersBean {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("active")
    @Expose
    private List<Active> active = null;
    @SerializedName("previous")
    @Expose
    private List<Previou> previous = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Active> getActive() {
        return active;
    }

    public void setActive(List<Active> active) {
        this.active = active;
    }

    public List<Previou> getPrevious() {
        return previous;
    }

    public void setPrevious(List<Previou> previous) {
        this.previous = previous;
    }
}
