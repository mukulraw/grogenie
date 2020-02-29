package app.tuton.grocery.cartPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class cartBean {
    @SerializedName("cart")
    @Expose
    private List<Cart> cart = null;
    @SerializedName("address")
    @Expose
    private List<Address> address = null;
    @SerializedName("offer")
    @Expose
    private String offer;

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }
}
