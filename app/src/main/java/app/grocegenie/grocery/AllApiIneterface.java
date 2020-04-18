package app.grocegenie.grocery;

import java.util.List;

import app.grocegenie.grocery.cartPOJO.cartBean;
import app.grocegenie.grocery.checkPromoPOJO.checkPromoBean;
import app.grocegenie.grocery.ordersPOJO.ordersBean;
import app.grocegenie.grocery.productListPOJO.productListBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

;

public interface AllApiIneterface {

    @GET("api/CategoryAllData.php")
    Call<List<productListBean>> CategoryAllData();

    @GET("api/loved.php")
    Call<List<productListBean>> loved();

    @Multipart
    @POST("api/search.php")
    Call<List<productListBean>> search(
            @Part("query") String query
    );

    @Multipart
    @POST("api/getCartData.php")
    Call<cartBean> getCartData(
            @Part("json") String json,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("api/addAddress.php")
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
    @POST("api/updateAddress.php")
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
    @POST("api/checkPromo.php")
    Call<checkPromoBean> checkPromoCode(
            @Part("promo") String promo,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("api/checkout.php")
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
    @POST("api/sendOTP.php")
    Call<loginBean> login(
            @Part("phone") String phone,
            @Part("token") String token,
            @Part("referral_code") String referral_code
    );

    @Multipart
    @POST("api/verifyOTP.php")
    Call<loginBean> verify(
            @Part("phone") String phone,
            @Part("otp") String otp
    );

    @Multipart
    @POST("api/getOrders.php")
    Call<ordersBean> getOrders(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("api/getOrderDetails.php")
    Call<ordersBean> getOrderDetails(
            @Part("id") String id
    );

    @Multipart
    @POST("api/addFeedback.php")
    Call<addMessageBean> addFeedback(
            @Part("user_id") String user_id,
            @Part("message") String message
    );

    @Multipart
    @POST("api/cancelOrder.php")
    Call<addMessageBean> cancelOrder(
            @Part("id") String id
    );

    @GET("api/getBanners.php")
    Call<List<bannerBean>> getBanners();

}
