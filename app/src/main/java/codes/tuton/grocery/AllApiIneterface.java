package codes.tuton.grocery;

import java.util.List;

import codes.tuton.grocery.cartPOJO.cartBean;
import codes.tuton.grocery.checkPromoPOJO.checkPromoBean;
import codes.tuton.grocery.ordersPOJO.ordersBean;
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
            @Part("name") String name,
            @Part("email") String email,
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
            @Part("name") String name,
            @Part("email") String email,
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
            @Part("saving") String saving,
            @Part("geniecash") String geniecash,
            @Part("delivery") String delivery,
            @Part("delivery_time") String delivery_time,
            @Part("promo_id") String promo_id,
            @Part("promovalue") String promovalue,
            @Part("grand") String grand,
            @Part("name") String name,
            @Part("email") String email,
            @Part("flat") String flat,
            @Part("landmark") String landmark,
            @Part("city") String city,
            @Part("area") String area,
            @Part("mobile") String mobile
    );

    @Multipart
    @POST("demo/sendOTP.php")
    Call<loginBean> login(
            @Part("phone") String phone,
            @Part("token") String token
    );

    @Multipart
    @POST("demo/verifyOTP.php")
    Call<loginBean> verify(
            @Part("phone") String phone,
            @Part("otp") String otp
    );

    @Multipart
    @POST("demo/getOrders.php")
    Call<ordersBean> getOrders(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("demo/getOrderDetails.php")
    Call<ordersBean> getOrderDetails(
            @Part("id") String id
    );

    @Multipart
    @POST("demo/addFeedback.php")
    Call<addMessageBean> addFeedback(
            @Part("user_id") String user_id,
            @Part("message") String message
    );

    @Multipart
    @POST("demo/cancelOrder.php")
    Call<addMessageBean> cancelOrder(
            @Part("id") String id
    );

    @GET("demo/getBanners.php")
    Call<List<bannerBean>> getBanners();

}
