package com.example.bandage.models

import com.google.gson.annotations.SerializedName

data class ResponseModel(

	@field:SerializedName("access_token")
	val response: String? = null
)
