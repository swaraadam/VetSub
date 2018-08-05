package muhammad.shulhi.muhibush.vetsub.services;



import java.util.ArrayList;

import muhammad.shulhi.muhibush.vetsub.model.Dokter;
import muhammad.shulhi.muhibush.vetsub.model.Event;
import muhammad.shulhi.muhibush.vetsub.model.Toko;
import muhammad.shulhi.muhibush.vetsub.model.User;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Afdolash on 11/13/2017.
 */

public class ApiServices {
//    server online
    public static String ip = "muhibush.xyz";

//    public static String BASE_URL = "http://"+ip+"/api_meet_point/index.php/Master/";
    public static String BASE_URL = "http://vetsub.herokuapp.com/api/";
    public static PostService service_post = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(ApiServices.PostService.class);

    public static GetService service_get = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(ApiServices.GetService.class);

    public static DeleteService service_delete = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(ApiServices.DeleteService.class);

    public interface PostService {
        @FormUrlEncoded
        @POST("login/client")
        Call<User> login(
                @Field("email") String email,
                @Field("password") String password
        );

        @FormUrlEncoded
        @POST("signup/client")
        Call<User> register(
                @Field("nama") String nama,
                @Field("email") String email,
                @Field("password") String password
        );
    }

    public interface GetService {
        @GET("petcare/getall")
        Call<ArrayList<Toko>> petcare_getall();

        @GET("dokter/getall")
        Call<ArrayList<Dokter>> dokter_getall();

        @GET("petcare/getone/{id}")
        Call<Toko> toko_getone(
                @Path("id") String id
        );

        @GET("dokter/getone/{id}")
        Call<Dokter> dokter_getone(
                @Path("id") String id
        );
    }

    public interface DeleteService {
    }

    public static String getIp() {
        return ip;
    }
}
