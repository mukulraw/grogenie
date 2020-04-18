package app.grocegenie.grocery.model;

public class ProductInfo {
    private String Pid, CategoryId, Pname, Qnt, PurchasePrice, UnitPriceKg, SellingPrice, Discount, Description, ImageName;

    public ProductInfo() {
    }

    public ProductInfo(String pid, String categoryId, String pname, String qnt, String purchasePrice, String unitPriceKg, String sellingPrice, String discount, String description, String imageName) {
        Pid = pid;
        CategoryId = categoryId;
        Pname = pname;
        Qnt = qnt;
        PurchasePrice = purchasePrice;
        UnitPriceKg = unitPriceKg;
        SellingPrice = sellingPrice;
        Discount = discount;
        Description = description;
        ImageName = imageName;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getQnt() {
        return Qnt;
    }

    public void setQnt(String qnt) {
        Qnt = qnt;
    }

    public String getPurchasePrice() {
        return PurchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        PurchasePrice = purchasePrice;
    }

    public String getUnitPriceKg() {
        return UnitPriceKg;
    }

    public void setUnitPriceKg(String unitPriceKg) {
        UnitPriceKg = unitPriceKg;
    }

    public String getSellingPrice() {
        return SellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        SellingPrice = sellingPrice;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }
}
