package com.example.farmerproducts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class login_fragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance() // Initialize Firebase Auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login_fragment, container, false)

        // Initialize views
        emailEditText = view.findViewById(R.id.emailedittext)
        passwordEditText = view.findViewById(R.id.passwordedittext)
        loginButton = view.findViewById(R.id.loginbtn)

        // Set login button click listener
        loginButton.setOnClickListener { performLogin() }

        return view
    }

    private fun performLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please fill in both email and password", Toast.LENGTH_SHORT).show()
            return
        }

        // Perform login
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    Log.d("LoginFragment", "Login success: ${auth.currentUser?.email}")
                } else {
                    // Login failed
                    Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    Log.e("LoginFragment", "Login error", task.exception)
                }
            }
    }
}
