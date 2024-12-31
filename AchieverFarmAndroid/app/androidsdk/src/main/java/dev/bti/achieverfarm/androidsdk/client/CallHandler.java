package dev.bti.achieverfarm.androidsdk.client;


import org.jetbrains.annotations.NotNull;

import dev.bti.achieverfarm.androidsdk.interfaces.OnFailureListener;
import dev.bti.achieverfarm.androidsdk.interfaces.OnSuccessListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallHandler<T> {
    private final Call<T> call;
    private OnSuccessListener<T> successListener;
    private OnFailureListener failureListener;

    public CallHandler(Call<T> call) {
        this.call = call;
    }

    public CallHandler<T> addOnSuccessListener(OnSuccessListener<T> listener) {
        this.successListener = listener;
        return this;
    }

    public CallHandler<T> addOnFailureListener(OnFailureListener listener) {
        this.failureListener = listener;
        return this;
    }

    public void execute() {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (successListener != null) {
                        successListener.onSuccess(response.body());
                    }
                } else if (response.isSuccessful()) {
                    if (successListener != null) {
                        successListener.onSuccess(null);
                    }
                } else {
                    if (failureListener != null) {
                        try {
                            assert response.errorBody() != null;
                            failureListener.onFailure(new Exception("API Error Body: " + response.errorBody().string()));
                        } catch (Exception e) {
                            failureListener.onFailure(new Exception("API Error: " + response));
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
                if (failureListener != null) {
                    failureListener.onFailure(new Exception(t));
                }
            }
        });
    }
}

