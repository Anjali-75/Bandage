package com.example.bandage.models

import com.google.gson.annotations.SerializedName

data class CartProductDeleteModel(

	@field:SerializedName("productId")
	val productId: String? = null
)
