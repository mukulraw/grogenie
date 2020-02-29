package app.tuton.grocery.offlineCartPOJO;

import java.util.ArrayList;
import java.util.List;

public class offlineCartBean {

    int itemCount = 0;
    public static float totalAmount = 0;
    public static int totalItem = 0;
    public static float totalSaved = 0;
    public static List<String> cartitems = new ArrayList<>();

    public static float getTotalAmount() {
        return totalAmount;
    }

    public static float getTotalSaved() {
        return totalSaved;
    }

    public static int getTotalItem() {
        return totalItem;
    }

    public static int getCount(String id)
    {
        int c = 0;
        for (int i = 0; i < cartitems.size(); i++) {
            if (id.equals(cartitems.get(i)))
            {
                c++;
            }
        }
        return c;
    }


}
