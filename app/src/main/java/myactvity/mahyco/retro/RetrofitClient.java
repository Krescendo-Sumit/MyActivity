package myactvity.mahyco.retro;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import myactvity.mahyco.BuildConfig;
import myactvity.mahyco.app.AppConstant;
import myactvity.mahyco.app.Prefs;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private Api myApi;
    private static Retrofit retrofit = null;
    static Context context;
    static Prefs mPref;
    private RetrofitClient(Context context) {
      /*  Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)

                .addConverterFactory(GsonConverterFactory.create())

                .build();*/
        this.context=context;
        mPref = Prefs.with(context);
          Retrofit retrofit = getRetrofitInstance(context);
        myApi = retrofit.create(Api.class);
    }

    public static Retrofit getRetrofitInstance(Context mContext) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(45, TimeUnit.SECONDS);
        httpClient.readTimeout(45, TimeUnit.SECONDS);
        httpClient.writeTimeout(45, TimeUnit.SECONDS);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(interceptor);
        httpClient.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request request = originalRequest.newBuilder()
                        //.header("Authorization", "Bearer " + Preferences.get(mContext, Preferences.KEY_ACCESS_TOKEN))
                    //    .header("Authorization", "Bearer " + mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""))
                        .header("Authorization", "Bearer gM-lQBzMe0-wC1bZlyemhTACiPhe1JNc-Z7XF-7wqQoSOvVkW-c8GcAJcJly5ygsuSF_x8uuXzRBj7xdSr5t2QeRYtU0cvpI0ueQOfCXQrVcq5lyw9Vjg-jt26uToGMLtwBP4b-wfjN1MRQImUn_AY2beBW-B_XFgCVSNEUpQez3igl5hS6UYIC9giYPi60bC6PfHO17OkkcFQBaSo8DBQ")
                        .header("Content-Type", "application/json")
                        .header("AppVersion", BuildConfig.VERSION_NAME)
                       // .header("JWTToken", Preferences.get(mContext, Preferences.KEY_JWT_TOKEN))
                        .method(originalRequest.method(), originalRequest.body())
                        .build();

                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpClient.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(RetroConstants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;


    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient(context);
        }
        return instance;
    }

    public Api getMyApi() {
        return myApi;
    }
}
