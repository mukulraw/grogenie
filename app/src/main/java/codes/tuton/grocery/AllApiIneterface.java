package codes.tuton.grocery;

import java.util.List;

import codes.tuton.grocery.cartPOJO.cartBean;
import codes.tuton.grocery.checkPromoPOJO.checkPromoBean;
import codes.tuton.grocery.productListPOJO.productListBean;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

;

public interface AllApiIneterface {

    @GET("demo/CategoryAllData.php")
    Call<List<productListBean>> CategoryAllData();

    @Multipart
    @POST("demo/search.php")
    Call<List<productListBean>> search(
            @Part("query") String query
    );

    @Multipart
    @POST("demo/getCartData.php")
    Call<cartBean> getCartData(
            @Part("json") String json,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("demo/addAddress.php")
    Call<addMessageBean> addAddress(
            @Part("user_id") String user_id,
            @Part("flat") String flat,
            @Part("landmark") String landmark,
            @Part("city") String city,
            @Part("area") String area,
            @Part("mobile") String mobile
    );

    @Multipart
    @POST("demo/updateAddress.php")
    Call<addMessageBean> updateAddress(
            @Part("user_id") String user_id,
            @Part("flat") String flat,
            @Part("landmark") String landmark,
            @Part("city") String city,
            @Part("area") String area,
            @Part("mobile") String mobile
    );

    @Multipart
    @POST("demo/checkPromoCode.php")
    Call<checkPromoBean> checkPromoCode(
            @Part("promo") String promo,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("demo/checkout.php")
    Call<addMessageBean> checkout(
            @Part("user_id") String user_id,
            @Part("products") String products,
            @Part("amount") String amount,
            @Part("geniecash") String geniecash,
            @Part("delivery") String delivery,
            @Part("promovalue") String promovalue,
            @Part("grand") String grand
    );

}
