package app.grocegenie.grocery.model;

public class CategoryInfo {
    private String CategoryId,CategoryName, Description, image;
    private ProductInfo product_info[];

    public CategoryInfo() {
    }

    public CategoryInfo(String categoryId, String categoryName, String description, String image, ProductInfo[] product_info) {
        CategoryId = categoryId;
        CategoryName = categoryName;
        Description = description;
        this.image = image;
        this.product_info = product_info;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public ProductInfo[] getProduct_info() {
        return product_info;
    }

    public void setProduct_info(ProductInfo[] product_info) {
        this.product_info = product_info;
    }
}
