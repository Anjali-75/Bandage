package com.example.bandage.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.bandage.Activity.Home
import com.example.bandage.R
import com.example.bandage.`interface`.UrlEndpoints
import com.example.bandage.models.LoginModel
import com.example.bandage.models.ResponseModel
import com.example.legalsatta.Services.RetrofitClass
import com.example.legalsatta.helper.NetworkCheck
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginFragment : Fragment() {

    private lateinit var v: View
    private lateinit var retrofit: Retrofit
    private lateinit var urlEndpoints: UrlEndpoints
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login_fregment, container, false)
        val loginBtn = v.findViewById<TextView>(R.id.loginBtn)
        val loginToRegisterBtn = v.findViewById<TextView>(R.id.registerText)
        val loginPassword = v.findViewById<TextView>(R.id.passwordTextBoxLogin)
        val loginEmail  = v.findViewById<TextView>(R.id.emailTextBoxLogin)

        loginToRegisterBtn.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.authFrame, RegisterFragment())
                ?.addToBackStack(null)
                ?.commit()
        }


        loginBtn.setOnClickListener{
            val userLoginData = LoginModel(
                loginPassword.text.toString(),
                loginEmail.text.toString()
            )
            initData()
            loginUser(userLoginData)

        }

        return v;
    }


    private fun initData() {

        /*Creating the instance of retrofit */
        retrofit = RetrofitClass.getInstance()

        /*Get the reference of Api interface*/
        urlEndpoints = retrofit.create(UrlEndpoints::class.java)
    }

    private fun loginUser(userData: LoginModel) {
        if(NetworkCheck.isConnected(context))
        {
            urlEndpoints.getUser(LoginModel(userData.password, userData.email))
                .enqueue(object : Callback< ResponseModel> {
                    override fun onResponse(
                        call: Call<ResponseModel>,
                        response: Response<ResponseModel>
                    ){
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                var token = response.body()?.response.toString()
                                println(token)

                                var pref = activity?.getSharedPreferences(
                                    "access_key_preference",
                                    Context.MODE_PRIVATE
                                )?.edit()

                                pref?.putString("token", token)?.commit()
                                startActivity(Intent(context, Home::class.java))
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                        Log.d("wait", "wait")
                    }
                })
//                        override fun onResponse(
//                            call: Call<RegistrationModel>,
//                            response: Response<RegistrationModel>
//                        ) {
//                            if (response.isSuccessful) {
//                                Toast.makeText(context, "Registered Successfully", Toast.LENGTH_LONG).show()
//                                activity?.supportFragmentManager
//                                    ?.beginTransaction()
//                                    ?.replace(R.id.authFrame, LoginFragment())
//                                    ?.addToBackStack(null)
//                                    ?.commit()
//
//                            } else {
//                                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
//                            }
//                        }
//
//                        override fun onFailure(call: Call<RegistrationModel>, t: Throwable) {
//                            t.printStackTrace()
//                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
//                        }
//                    })
        }
        else
        {
            Toast.makeText(context, "Please check you internet connection", Toast.LENGTH_LONG).show()
        }
    }

}