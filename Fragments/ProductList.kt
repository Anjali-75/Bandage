package com.example.bandage.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bandage.Activity.Cart
import com.example.bandage.Activity.Product
import com.example.bandage.R
import com.example.bandage.`interface`.UrlEndpoints
import com.example.bandage.models.CartModel
import com.example.bandage.models.ProductModel
import com.example.bandage.models.Products
import com.example.legalsatta.Services.RetrofitClass
import com.example.legalsatta.helper.NetworkCheck
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class ProductList : Fragment() {

    private lateinit var v: View
    private  lateinit var retrofit:Retrofit
    private lateinit var urlEndpoints:UrlEndpoints
    lateinit var productListView:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_product_list, container, false)
        productListView = v.findViewById(R.id.product_list)
        initData()
        getProducts();

        var cartbtn = v.findViewById<ImageView>(R.id.cart)
        cartbtn.setOnClickListener{
            var  intent = Intent(context, Cart::class.java)
            startActivity(intent)
        }

        return v;
    }


    private fun getProducts(){
        if(NetworkCheck.isConnected(context)){
            urlEndpoints.getAllProducts().enqueue(
                object : Callback<ProductModel>{
                    override fun onResponse(
                        call: Call<ProductModel>,
                        response: Response<ProductModel>
                    ) {

                        if(response.isSuccessful){
                            val productAdapter = context?.let{ response.body()
                                ?.let { it1 -> ProductAdapter(it, it1.products) } }
                            productListView.layoutManager = GridLayoutManager(context, 2)
                            productListView.adapter = productAdapter
                        }
                    }

                    override fun onFailure(call: Call<ProductModel>, t: Throwable) {
                        Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                    }

                }
            )
        }
    }

    private fun initData() {

        /*Creating the instance of retrofit */
        retrofit = RetrofitClass.getInstance()

        /*Get the reference of Api interface*/
        urlEndpoints = retrofit.create(UrlEndpoints::class.java)
    }


    class ProductAdapter(var context: Context, var ProductList: ArrayList<Products?>): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

        class ProductViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
            var productNameTextView = itemView.findViewById<TextView>(R.id.prod_name)
            var productImg = itemView.findViewById<ImageView>(R.id.img)
            var categoryTextView = itemView.findViewById<TextView>(R.id.category)
            var priceTextView = itemView.findViewById<TextView>(R.id.price)
            var addToCartBtn = itemView.findViewById<TextView>(R.id.addToCartBtn)
        }

        fun getList(updatedList: ArrayList<Products>){
            for(idx in ProductList.indices){
                ProductList[idx] = updatedList[idx]
            }
            this.notifyDataSetChanged();
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder{
            val view = LayoutInflater.from(context). inflate(
                R.layout.product_card, parent,
                false
            )
            return ProductViewHolder(view)
        }

        override fun getItemCount(): Int {
            return ProductList.size
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            var productModel = ProductList[position]
            if (productModel != null) {
                holder.productNameTextView.text = productModel.name
                holder.categoryTextView.text = productModel.category
                holder.priceTextView.text = "Rs. ${productModel.price}"
                productModel.images?.get(0)?.let { setImage(context,holder.productImg, it) }
            }

            holder.itemView.setOnClickListener{
                print("abc")
                var  intent = Intent(context, Product::class.java)
                var bundle = Bundle()
                if (productModel != null) {
                    intent.putExtra("name", productModel.name)
                    intent.putExtra("img1", productModel.images?.get(0))
                    intent.putExtra("img2", productModel.images?.get(1))
                    if(productModel.images?.size != 3) {
                        intent.putExtra("img3",R.drawable.ic_launcher_foreground)
                    }else{
                        intent.putExtra("img3", productModel.images?.get(2))
                    }
                    intent.putExtra("category", productModel.category)
                    intent.putExtra("price", "${productModel.price}")
                    intent.putExtra("desc", productModel.description)
                    intent.putExtra("id", productModel.productId)
                }
                startActivity(context, intent, bundle)
//                activityResultLauncher.launch(intent)
            }
//
//            if(!productModel.isAdded){
//                holder.addToCartBtn.text = "Add"
//                holder.addToCartBtn.setOnClickListener{
//                    holder.addToCartBtn.text = "Go to Cart"
//                    Cartfun.addItem(productModel)
//                    productModel.isAdded= true;
//                    this.notifyItemChanged(position)
//                }
//            }
//            else{
//                holder.addToCartBtn.text = "Go To Cart"
//                holder.addToCartBtn.setOnClickListener{
//                    var sharedpreferences: SharedPreferences = getSharedPreferences("key", Context.MODE_PRIVATE);
//                    val quantity = 1;
//                    val productId = productModel.productId
//                    val cartData = CartModel(
//                        quantity.toString(),
//                        productId
//                    )
//                    initData()
//                    sharedpreferences.getString("token","")?.let { it1 -> addToCart(it1,cartData) }
//                    addToCartBtn.setText("Go to Cart")
//                }
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