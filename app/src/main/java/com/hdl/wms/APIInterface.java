package com.hdl.wms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {
    @POST("/tarefas")
    Call<LoginResponse> createUser(@Body LoginResponse login);
}
