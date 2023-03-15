package com.example.bandage.models

import com.google.gson.annotations.SerializedName

data class PlaceOrderModel(

	@field:SerializedName("details")
	val details: List<DetailsItem?>? = null
)

data class DetailsItem(

	@field:SerializedName("quantity")
	val quantity: String? = null,

	@field:SerializedName("productId")
	val productId: String? = null
)
