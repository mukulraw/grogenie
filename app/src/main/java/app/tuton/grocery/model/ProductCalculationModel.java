package app.tuton.grocery.model;

public class ProductCalculationModel {
    int itemCount = 0;
    public static float totalAmount = 0;
    public static int totalItem = 0;
    public static float totalSaved = 0;

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}
