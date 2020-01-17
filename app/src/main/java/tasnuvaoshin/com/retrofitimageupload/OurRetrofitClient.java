package tasnuvaoshin.com.retrofitimageupload;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OurRetrofitClient {

    @POST("retrofit.php")
    @FormUrlEncoded
    Call<ResponseClass> UploadImage(@Field("name") String name,@Field("image") String image);
}
