package com.example.bandage.Activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bandage.*
import com.example.bandage.`interface`.UrlEndpoints
import com.example.bandage.models.CartProductDeleteModel
import com.example.bandage.models.CartProductModelItem
import com.example.legalsatta.Services.RetrofitClass
import com.example.legalsatta.helper.NetworkCheck
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class Cart : AppCompatActivity() {

    private lateinit var retrofit: Retrofit
    private lateinit var urlEndpoints:UrlEndpoints
    private lateinit var recycleViewCart:RecyclerView
    //var sharedpreferences: SharedPreferences = getSharedPreferences("access_key_preference", Context.MODE_PRIVATE);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        initData()
        var sharedpreferences: SharedPreferences = getSharedPreferences("access_key_preference", Context.MODE_PRIVATE);
        sharedpreferences.getString("token"," ")?.let { fetchCart(it) }


    }
    private fun initData() {

        /*Creating the instance of retrofit */
        retrofit = RetrofitClass.getInstance()

        /*Get the reference of Api interface*/
        urlEndpoints = retrofit.create(UrlEndpoints::class.java)
    }

    private fun fetchCart(access_token: String){
        if(NetworkCheck.isConnected(this))
        {
            urlEndpoints.getProduct(access_token)
                .enqueue(object : Callback<ArrayList<CartProductModelItem>> {
                    override fun onResponse(
                        call: Call<ArrayList<CartProductModelItem>>,
                        response: Response<ArrayList<CartProductModelItem>>
                    ) {
                        if (response.isSuccessful) {
                            println("response is" + response.body())
                            recycleViewCart = findViewById(R.id.recycleViewCart)
                            recycleViewCart.layoutManager = GridLayoutManager(this@Cart,1)
                            recycleViewCart.adapter =
                                response.body()?.let { CartAdapter(this@Cart, it) }
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<CartProductModelItem>>, t: Throwable) {
                        t.printStackTrace()
                        Toast.makeText(this@Cart, "An error Occurred", Toast.LENGTH_LONG).show()
                    }
                })
        }
        else
        {
            Toast.makeText(this, "Please check you internet connection", Toast.LENGTH_LONG).show()
        }
    }

    inner class CartAdapter(var context: Context, var cartList: ArrayList<CartProductModelItem>): RecyclerView.Adapter<CartAdapter.ProductViewHolder>(){

       inner class ProductViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
           var spinner: Spinner = itemView.findViewById(R.id.dropdown_menu)
           var productNameTextView = itemView.findViewById<TextView>(R.id.prodname)
            var categoryTextView = itemView.findViewById<TextView>(R.id.prodcategory)
            var priceTextView = itemView.findViewById<TextView>(R.id.prodprice)
            var img = itemView.findViewById<ImageView>(R.id.prodimg)
            var deleteCartbtn = itemView.findViewById<TextView>(R.id.deleteCart)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder{
            val view = LayoutInflater.from(context). inflate(
                R.layout.cart_product, parent,
                false
            )
            return ProductViewHolder(view)
        }

        override fun getItemCount(): Int {
            return cartList.size
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            var productModel = cartList[position]
            if (productModel != null) {
                holder.productNameTextView.text = productModel.name
                holder.categoryTextView.text = productModel.category
                holder.priceTextView.text = "Rs. ${productModel.price}"
                productModel.images?.get(0)?.let { setImage(context, holder.img, it) }
            }

            Log.d("msggg", R.array.num_array.toString())

            ArrayAdapter.createFromResource(
                        context,
                        R.array.num_array,
                        android.R.layout.simple_spinner_item
                    ).also {
                    adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        holder.spinner.adapter = adapter
                    }

            holder.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> Toast.makeText(parent.context, "Spinner item 1!", Toast.LENGTH_SHORT)
                            .show()
                        1 -> Toast.makeText(parent.context, "Spinner item 2!", Toast.LENGTH_SHORT)
                            .show()
                        2 -> Toast.makeText(parent.context, "Spinner item 3!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                    // sometimes you need nothing here
                }
            }
            holder.deleteCartbtn.setOnClickListener {
                initData()
                var sharedpreferences: SharedPreferences = getSharedPreferences("access_key_preference", Context.MODE_PRIVATE);


                val cartDeleteData = CartProductDeleteModel(
                    productModel.productId
                )
                sharedpreferences.getString("token", "")
                    ?.let { it1 -> deleteCartProduct(it1, cartDeleteData,holder) }
            }
        }

        private fun deleteCartProduct(access_token: String, cartDeleteModel: CartProductDeleteModel, holder: ProductViewHolder) {
                if (NetworkCheck.isConnected(context)) {
                    urlEndpoints.deleteProduct(access_token, cartDeleteModel)
                        .enqueue(object : Callback<CartProductDeleteModel> {
                            override fun onResponse(
                                call: Call<CartProductDeleteModel>,
                                response: Response<CartProductDeleteModel>
                            ) {
                                if (response.isSuccessful) {
                                    val deleteProduct = cartList.find { product -> product.productId == cartDeleteModel.productId }
                                    cartList.remove(deleteProduct)
                                    notifyDataSetChanged()
                                }
                            }
                            override fun onFailure(
                                call: Call<CartProductDeleteModel>,
                                t: Throwable
                            ) {
                                t.printStackTrace()
                                Toast.makeText(context, "An error Occurred", Toast.LENGTH_LONG)
                                    .show()
                            }
                        })
                } else {
                    Toast.makeText(this@Cart, "Please check you internet connection", Toast.LENGTH_LONG)
                        .show()
                }
            }

        private fun setImage(context: Context?, imageView: ImageView, imgURl: String){
            Glide
                .with(context!!)
                .load(imgURl)
                .fitCenter()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView)
        }
        }
    }
