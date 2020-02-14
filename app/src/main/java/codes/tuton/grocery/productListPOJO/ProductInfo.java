package codes.tuton.grocery.productListPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductInfo {
    @SerializedName("Pid")
    @Expose
    private String pid;
    @SerializedName("CategoryId")
    @Expose
    private String categoryId;
    @SerializedName("Pname")
    @Expose
    private String pname;
    @SerializedName("Qnt")
    @Expose
    private String qnt;
    @SerializedName("PurchasePrice")
    @Expose
    private String purchasePrice;
    @SerializedName("UnitPriceKg")
    @Expose
    private String unitPriceKg;
    @SerializedName("SellingPrice")
    @Expose
    private String sellingPrice;
    @SerializedName("Discount")
    @Expose
    private String discount;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("ImageName")
    @Expose
    private String imageName;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("offer")
    @Expose
    private String offer;
    @SerializedName("offerprice")
    @Expose
    private String offerprice;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getQnt() {
        return qnt;
    }

    public void setQnt(String qnt) {
        this.qnt = qnt;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getUnitPriceKg() {
        return unitPriceKg;
    }

    public void setUnitPriceKg(String unitPriceKg) {
        this.unitPriceKg = unitPriceKg;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getOfferprice() {
        return offerprice;
    }

    public void setOfferprice(String offerprice) {
        this.offerprice = offerprice;
    }
}
