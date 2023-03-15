package com.example.bandage.models

import com.google.gson.annotations.SerializedName

data class LoginModel(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
