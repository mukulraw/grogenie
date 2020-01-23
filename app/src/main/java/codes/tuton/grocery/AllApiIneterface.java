package codes.tuton.grocery;

import java.util.List;

import codes.tuton.grocery.cartPOJO.cartBean;
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

}
