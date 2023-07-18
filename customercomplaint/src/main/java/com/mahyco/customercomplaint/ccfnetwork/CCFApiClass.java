package com.mahyco.customercomplaint.ccfnetwork;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.mahyco.customercomplaint.BuildConfig;
import com.mahyco.customercomplaint.CCFConstantValues;
import com.mahyco.customercomplaint.ccfinterfaces.CCFAllListListener;
import com.mahyco.customercomplaint.ccfinterfaces.CCFCategoryInterface;
import com.mahyco.customercomplaint.ccfinterfaces.CCFLotNumberInterface;
import com.mahyco.customercomplaint.ccfinterfaces.CCFRegstComplntInterface;
import com.mahyco.customercomplaint.ccfinterfaces.CCFSbmtCmplntInterface;
import com.mahyco.customercomplaint.ccfinterfaces.CCFViewCmplntInterface;
import com.mahyco.customercomplaint.ccfstoredata.CCFStoreData;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CCFApiClass {
    private static Retrofit getRetrofit(Context context) {
       // Log.e("temporary", "version " + android.os.Build.VERSION.SDK_INT);
        if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP) {
           // Log.e("temporary","version EQUAL TO 21 "+ android.os.Build.VERSION.SDK_INT);
            Retrofit retrofit;
            retrofit = new Retrofit.Builder()
                    .baseUrl(CCFConstantValues.CCF_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getUnsafeOkHttpClient(context).build())
                    .build();
            return retrofit;
        } else {
          //  Log.e("temporary","version NOT EQUAL TO  21 "+ android.os.Build.VERSION.SDK_INT);
            Retrofit retrofit;
            retrofit = new Retrofit.Builder()
                    .baseUrl(CCFConstantValues.CCF_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(init(context).build())
                    .build();
            return retrofit;
        }
    }

    private static OkHttpClient.Builder init(Context context) {

        HttpLoggingInterceptor logging;
        OkHttpClient.Builder httpClient;

        logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        httpClient = new OkHttpClient.Builder();

        //Create a new Interceptor.
        Interceptor headerAuthorizationInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request();
                Headers headers = request.headers().newBuilder().add("Authorization", "Bearer " + CCFStoreData.getString(context, CCFConstantValues.CCF_CUST_TOKEN)).build();
                request = request.newBuilder().headers(headers).build();
                return chain.proceed(request);
            }
        };
        //Add the interceptor to the client builder.
        httpClient.addInterceptor(headerAuthorizationInterceptor);
        httpClient.addInterceptor(logging).readTimeout(240, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS);

        return httpClient;
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient(Context context) {
        try {
            // Create a trust manager that does not validate certificate chains
            @SuppressLint("CustomX509TrustManager") final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG) {
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            }
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //Create a new Interceptor.
            Interceptor headerAuthorizationInterceptor = new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    okhttp3.Request request = chain.request();
                    Headers headers = request.headers().newBuilder().add("Authorization", "Bearer " + CCFStoreData.getString(context, CCFConstantValues.CCF_CUST_TOKEN)).build();
                    request = request.newBuilder().headers(headers).build();
                    return chain.proceed(request);
                }
            };
            //Add the interceptor to the client builder.
            builder.addInterceptor(headerAuthorizationInterceptor);
            builder.addInterceptor(logging).readTimeout(240, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS);

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void getRegstComplaintResponse(final CCFRegstComplntInterface.ResponseFromServer responseFromServer
            , JsonObject jsonObject, Context context) {
        CCFApiInterface api = getRetrofit(context).create(CCFApiInterface.class);
        Call<CCFBaseApiResponse> request = api.complaintResponse(jsonObject);
        request.enqueue(new Callback<CCFBaseApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<CCFBaseApiResponse> call, @NonNull Response<CCFBaseApiResponse> response) {
                responseFromServer.onModelInterfaceSuccess(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<CCFBaseApiResponse> call, @NonNull Throwable t) {
                responseFromServer.onModelInterfaceFailure(t);
            }
        });
    }


    public static void getCategoryResponse(final CCFCategoryInterface.ResponseFromServer responseFromServer, Context context) {
        CCFApiInterface api = getRetrofit(context).create(CCFApiInterface.class);
        Call<CCFCategoryPojoModel> request = api.getCategories();
        request.enqueue(new Callback<CCFCategoryPojoModel>() {
            @Override
            public void onResponse(@NonNull Call<CCFCategoryPojoModel> call, @NonNull Response<CCFCategoryPojoModel> response) {
                responseFromServer.onModelInterfaceSuccess(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<CCFCategoryPojoModel> call, @NonNull Throwable t) {
                responseFromServer.onModelInterfaceFailure(t);
            }
        });
    }

    public static void getLotNumberResponse(final CCFLotNumberInterface.ResponseFromServer responseFromServer, /*String lotNumber*/JsonObject jsonObject, Context context) {
        CCFApiInterface api = getRetrofit(context).create(CCFApiInterface.class);
        Call<CCFLotPojoModel> request = api.getLotNumberDetails(/*lotNumber*/jsonObject);
        request.enqueue(new Callback<CCFLotPojoModel>() {
            @Override
            public void onResponse(@NonNull Call<CCFLotPojoModel> call, @NonNull Response<CCFLotPojoModel> response) {
                responseFromServer.onLotModelInterfaceSuccess(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<CCFLotPojoModel> call, @NonNull Throwable t) {
                responseFromServer.onLotModelInterfaceFailure(t);
            }
        });
    }

    static ProgressDialog progressDialog;


    public static void getState(CCFAllListListener ccfAllListListener, Context context) {
        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wait..");

            if (!progressDialog.isShowing())
                progressDialog.show();

            CCFApiInterface api = getRetrofit(context).create(CCFApiInterface.class);
            Call<CCFStatePojoModel> request = api.getStateList();
            request.enqueue(new Callback<CCFStatePojoModel>() {
                @Override
                public void onResponse(@NonNull Call<CCFStatePojoModel> call, @NonNull Response<CCFStatePojoModel> response) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            CCFStatePojoModel result = response.body();
                            try {
                                ccfAllListListener.onStateListResponse(result);
                            } catch (Exception e) {
                                ccfAllListListener.onFailure(result.getMessage(), null);
                            }
                        } else {
                            ccfAllListListener.onFailure("", null);
                        }
                    } else {
                        /*01/6/2023 commented*/
                        //  ccfAllListListener.onFailure("", null);
                        /*01/6/2023 commented ended here*/
                        /*01/6/2023 added*/
                        if (response.code() == 401) {
                            ccfAllListListener.onFailure("Your session has expired and you need to login again.", null);
                        } else {
                            ccfAllListListener.onFailure("", null);
                        }
                        /*01/6/2023 added ended here*/
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CCFStatePojoModel> call, @NonNull Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    ccfAllListListener.onFailure("", t);
                }
            });
        } catch (Exception e) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            ccfAllListListener.onFailure("", e);
        }
    }


    public static void getDistrict(CCFAllListListener ccfAllListListener, Context context,
            /*String stateCode*/JsonObject jsonObject) {
        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wait..");

            if (!progressDialog.isShowing())
                progressDialog.show();

            CCFApiInterface api = getRetrofit(context).create(CCFApiInterface.class);
            Call<CCFDistrictPojoModel> request = api.getDistrictList(/*stateCode*/jsonObject);
            request.enqueue(new Callback<CCFDistrictPojoModel>() {
                @Override
                public void onResponse(@NonNull Call<CCFDistrictPojoModel> call, @NonNull Response<CCFDistrictPojoModel> response) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            CCFDistrictPojoModel result = response.body();
                            try {
                                ccfAllListListener.onDistrictListResponse(result);
                            } catch (Exception e) {
                                ccfAllListListener.onFailure(result.getMessage(), null);
                            }
                        } else {
                            ccfAllListListener.onFailure("", null);
                        }
                    } else {
                        /*01/6/2023 commented*/
                        //  ccfAllListListener.onFailure("", null);
                        /*01/6/2023 commented ended here*/
                        /*01/6/2023 added*/
                        if (response.code() == 401) {
                            ccfAllListListener.onFailure("Your session has expired and you need to login again.", null);
                        } else {
                            ccfAllListListener.onFailure("", null);
                        }
                        /*01/6/2023 added ended here*/
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CCFDistrictPojoModel> call, @NonNull Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    ccfAllListListener.onFailure("", t);
                }
            });
        } catch (Exception e) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            ccfAllListListener.onFailure("", e);
        }
    }


    public static void getTaluka(CCFAllListListener ccfAllListListener, Context context,
                                 JsonObject jsonObject) {
        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wait..");

            if (!progressDialog.isShowing())
                progressDialog.show();

            CCFApiInterface api = getRetrofit(context).create(CCFApiInterface.class);
            Call<CCFTalukaPojoModel> request = api.getTalukaList(jsonObject);
            request.enqueue(new Callback<CCFTalukaPojoModel>() {
                @Override
                public void onResponse(@NonNull Call<CCFTalukaPojoModel> call, @NonNull Response<CCFTalukaPojoModel> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            CCFTalukaPojoModel result = response.body();
                            try {
                                ccfAllListListener.onTalukaListResponse(result);
                            } catch (Exception e) {
                                ccfAllListListener.onFailure(result.getMessage(), null);
                            }
                        } else {
                            ccfAllListListener.onFailure("", null);
                        }
                    } else {
                        /*01/6/2023 commented*/
                        //  ccfAllListListener.onFailure("", null);
                        /*01/6/2023 commented ended here*/
                        /*01/6/2023 added*/
                        if (response.code() == 401) {
                            ccfAllListListener.onFailure("Your session has expired and you need to login again.", null);
                        } else {
                            ccfAllListListener.onFailure("", null);
                        }
                        /*01/6/2023 added ended here*/
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CCFTalukaPojoModel> call, @NonNull Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    ccfAllListListener.onFailure("", t);
                }
            });
        } catch (Exception e) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            ccfAllListListener.onFailure("", e);
        }
    }

    public static void getDepot(CCFAllListListener ccfAllListListener, Context context,
                                JsonObject jsonObject) {
        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wait..");

            if (!progressDialog.isShowing())
                progressDialog.show();

            CCFApiInterface api = getRetrofit(context).create(CCFApiInterface.class);
            Call<CCFDepotPojoModel> request = api.getDepot(jsonObject);
            request.enqueue(new Callback<CCFDepotPojoModel>() {
                @Override
                public void onResponse(@NonNull Call<CCFDepotPojoModel> call, @NonNull Response<CCFDepotPojoModel> response) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            CCFDepotPojoModel result = response.body();
                            try {
                                ccfAllListListener.onDepotListResponse(result);
                            } catch (Exception e) {
                                ccfAllListListener.onFailure(result.getMessage(), null);
                            }
                        } else {
                            ccfAllListListener.onFailure("", null);
                        }
                    } else {
                        try {
                            /*01/6/2023 added*/
                            if (response.code() == 401) {
                                ccfAllListListener.onFailure("Your session has expired and you need to login again.", null);
                            } else /*01/6/2023 added ended here*/
                                if (response.body() != null) {
                                    CCFDepotPojoModel result = response.body();
                                    ccfAllListListener.onFailure(result.getMessage(), null);
                                } else {
                                    ccfAllListListener.onFailure("", null);
                                }
                        } catch (Exception e) {
                            ccfAllListListener.onFailure("", null);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CCFDepotPojoModel> call, @NonNull Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    ccfAllListListener.onFailure("", t);
                }
            });
        } catch (Exception e) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            ccfAllListListener.onFailure("", e);
        }
    }

    public static void getViewCmplntResponse(final CCFViewCmplntInterface.ResponseFromServer responseFromServer, /*String tbmCode*/JsonObject jsonObject, Context context) {
        CCFApiInterface api = getRetrofit(context).create(CCFApiInterface.class);
        Call<CCFViewCmplntPojoModel> request = api.getViewComplaint(/*tbmCode*/jsonObject);
        request.enqueue(new Callback<CCFViewCmplntPojoModel>() {
            @Override
            public void onResponse(@NonNull Call<CCFViewCmplntPojoModel> call, @NonNull Response<CCFViewCmplntPojoModel> response) {
                responseFromServer.onLotModelInterfaceSuccess(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<CCFViewCmplntPojoModel> call, @NonNull Throwable t) {
                responseFromServer.onLotModelInterfaceFailure(t);
            }
        });
    }

    public static void getPendingCmplntResponse(final CCFViewCmplntInterface.ResponseFromServer responseFromServer, /*String tbmCode*/JsonObject jsonObject, Context context) {
        CCFApiInterface api = getRetrofit(context).create(CCFApiInterface.class);
        Call<CCFPendingCmplntPojoMOdel> request = api.getPendingComplaint(/*rbmCode*/jsonObject);
        request.enqueue(new Callback<CCFPendingCmplntPojoMOdel>() {
            @Override
            public void onResponse(@NonNull Call<CCFPendingCmplntPojoMOdel> call, @NonNull Response<CCFPendingCmplntPojoMOdel> response) {
                responseFromServer.onLotModelInterfaceSuccess(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<CCFPendingCmplntPojoMOdel> call, @NonNull Throwable t) {
                responseFromServer.onLotModelInterfaceFailure(t);
            }
        });
    }

    public static void getSubmitPendingCmplntResponse(final CCFSbmtCmplntInterface.ResponseFromServer responseFromServer, JsonObject jsonObject, Context context) {
        CCFApiInterface api = getRetrofit(context).create(CCFApiInterface.class);
        Call<CCFBaseApiResponse> request = api.getSubmitPendingComplaint(jsonObject);
        request.enqueue(new Callback<CCFBaseApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<CCFBaseApiResponse> call, @NonNull Response<CCFBaseApiResponse> response) {
                responseFromServer.onSuccess(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<CCFBaseApiResponse> call, @NonNull Throwable t) {
                responseFromServer.onFailure(t);
            }
        });
    }
}
