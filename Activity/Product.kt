package com.example.bandage.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource
import com.example.bandage.Fragments.LoginFragment
import com.example.bandage.R
import com.example.bandage.`interface`.UrlEndpoints
import com.example.bandage.models.CartModel
import com.example.bandage.models.LoginModel
import com.example.bandage.models.RegistrationModel
import com.example.legalsatta.Services.RetrofitClass
import com.example.legalsatta.helper.NetworkCheck
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class Product : AppCompatActivity() {

    private lateinit var retrofit: Retrofit
    private lateinit var urlEndpoints: UrlEndpoints;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val name = findViewById<TextView>(R.id.name)
        val price = findViewById<TextView>(R.id.price)
        val desc = findViewById<TextView>(R.id.desc)
        val mainImg = findViewById<ImageView>(R.id.img_main)
        val img1 = findViewById<ImageView>(R.id.img_small1)
        val img2 = findViewById<ImageView>(R.id.img_small2)
        val addToCartBtn = findViewById<TextView>(R.id.addToCartBtnInside)

        name.setText(intent.getStringExtra("name"))
        price.setText(intent.getStringExtra("price"))
        desc.setText(intent.getStringExtra("desc"))
        intent.getStringExtra("img1")?.let { setImage(this, mainImg, it) }
        intent.getStringExtra("img2")?.let { setImage(this, img1, it) }
        intent.getStringExtra("img3")?.let { setImage(this, img2, it) }

        img1.setOnClickListener{
            val mainDrawable=  mainImg.drawable
            intent.getStringExtra("img2")?.let { setImage(this, mainImg, it) }
            img1.setImageDrawable(mainDrawable)
        }
        img2.setOnClickListener{
            val mainDrawable=  mainImg.drawable
            intent.getStringExtra("img3")?.let { setImage(this, mainImg, it) }
            img2.setImageDrawable(mainDrawable)
        }

        addToCartBtn.setOnClickListener {
                var sharedpreferences: SharedPreferences = getSharedPreferences("access_key_preference", Context.MODE_PRIVATE);
                val quantity = 1;
                val productId = intent.getStringExtra("id")
                 val cartData = CartModel(
                    quantity.toString(),
                     productId
                 )
                initData()
            sharedpreferences.getString("token","")?.let { it1 -> addToCart(it1,cartData) }
            addToCartBtn.setText("Go to Cart")
        }
    }

    private fun initData() {

        /*Creating the instance of retrofit */
        retrofit = RetrofitClass.getInstance()

        /*Get the reference of Api interface*/
        urlEndpoints = retrofit.create(UrlEndpoints::class.java)
    }

    private fun addToCart(access_token:String, cartData: CartModel) {
        if(NetworkCheck.isConnected(this))
        {
            urlEndpoints.addProduct(access_token, CartModel(cartData.quantity, cartData.productId))
                .enqueue(object : Callback<CartModel> {
                    override fun onResponse(
                        call: Call< CartModel>,
                        response: Response<CartModel>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@Product, "Item Added to Cart", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<CartModel>, t: Throwable) {
                        t.printStackTrace()
                        Toast.makeText(this@Product, "An error Occurred", Toast.LENGTH_LONG).show()
                    }
                })
        }
        else
        {
            Toast.makeText(this, "Please check you internet connection", Toast.LENGTH_LONG).show()
        }
    }

    private fun setImage(context: Context?, imageView: ImageView, imgURl: String) {
        Glide
            .with(context!!)
            .load(imgURl)
            .fitCenter()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(imageView)
    }
}