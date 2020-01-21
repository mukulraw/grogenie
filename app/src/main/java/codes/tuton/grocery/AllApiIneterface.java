package codes.tuton.grocery;

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
    Call<productListBean> CategoryAllData();




}
