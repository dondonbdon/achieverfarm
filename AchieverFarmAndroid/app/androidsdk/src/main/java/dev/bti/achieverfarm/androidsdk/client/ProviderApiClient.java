package dev.bti.achieverfarm.androidsdk.client;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Instant;

import dev.bti.achieverfarm.androidsdk.common.Config;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProviderApiClient {
    private static final String BASE_URL = String.format(" http://%s/api/v1/", Config.GetInstance().getServerAddress());
    private static Retrofit retrofitNoInterceptor, retrofitInterceptor;

    public static <T> T GetInstance(Class<T> t, String jwtToken) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .create();

        if (retrofitInterceptor == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(jwtToken))
                    .build();

            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(BASE_URL);
            builder.addConverterFactory(GsonConverterFactory.create(gson));
            builder.client(client);

            retrofitInterceptor = builder.build();
        }

        return retrofitInterceptor.create(t);
    }

    public static <T> T GetInstance(Class<T> t) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .create();

        if (retrofitNoInterceptor == null) {

            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(BASE_URL);
            builder.addConverterFactory(GsonConverterFactory.create(gson));

            retrofitNoInterceptor = builder.build();
        }

        return retrofitNoInterceptor.create(t);
    }
}

