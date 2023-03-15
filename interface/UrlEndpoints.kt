package com.example.bandage.`interface`

import com.example.bandage.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST

interface UrlEndpoints {

    @POST("/auth/registration")
    fun createUser(@Body params: RegistrationModel?): Call<RegistrationModel>

    @GET("/getProducts")
    fun getAllProducts(): Call<ProductModel>

    //@Headers("authorization":shared prefrenve)
    @POST("/auth/login")
    fun getUser( @Body params: LoginModel?): Call<ResponseModel>


    @POST("/cart/addProduct")
    fun addProduct(@Header("authorization") jwtToken :String , @Body params: CartModel?) : Call< CartModel>

    @GET("/cart/getProduct")
    fun getProduct(@Header("authorization") jwtToken: String): Call<ArrayList<CartProductModelItem>>

    @POST("/cart/deleteProduct")
    fun deleteProduct(@Header("authorization") jwtToken: String, @Body params: CartProductDeleteModel): Call<CartProductDeleteModel>

   }


