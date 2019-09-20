package com.example.chatapplication.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-type:application/json",
                    "Authorization:key=AAAABAzwszs:APA91bHrxyQjbVfP7YyW6kCFX9s3lL4uZPTZ5LYsmztalty8Rm0dDtDXa6u-LqGg7TFAzowoUmEpJJESd9Wr7-vVRSaCfHb0zCwRdpsVl_IxTPvkJOowb6-5lO3JOF9zcYWdUKARcyQv"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sentNotification(@Body Sender body);
}
