package com.example.hifa.twilioapi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface TwilioAPIService {

    @POST("/prod/sms")
    Call<Void> createPost(@Header("x-api-key") String apiKey, @Body SendMessageRequest sendMessageRequest);
}
