package com.example.farmerproducts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class Login_Signup_Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login__signup_, container, false)
        val loginbtn = view.findViewById<Button>(R.id.loginbtnmain)
        val signupbtn = view.findViewById<Button>(R.id.signupbtnmain)

        val navController = findNavController()

        loginbtn.setOnClickListener {
            navController.navigate(R.id.action_login_Signup_Fragment_to_login_fragment)
        }
        signupbtn.setOnClickListener {
            navController.navigate(R.id.action_login_Signup_Fragment_to_signup_fragment)
        }
        return view
    }
}
