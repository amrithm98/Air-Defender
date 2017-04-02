package shreshta.com.air_defender.Utils;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import shreshta.com.air_defender.Models.User;

/**
 * Created by amrith on 4/2/17.
 */

public interface RestApiInterface {
    @FormUrlEncoded
    @POST("admin/auth/login")
    Call<User> login(@Field("idToken") String idToken);

    @FormUrlEncoded
    @POST("admin/auth/register")
    Call <User> fcmUpdate(@Header("x-auth-token")String idToken, @Field("fcmId")String fcm);
}
