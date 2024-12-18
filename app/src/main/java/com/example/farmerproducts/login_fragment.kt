package com.example.farmerproducts

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

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

        // Validate email and password locally
        if (email.isEmpty()) {
            emailEditText.error = "Email is required"
            emailEditText.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Please enter a valid email"
            emailEditText.requestFocus()
            return
        }

        if (password.isEmpty()) {
            passwordEditText.error = "Password is required"
            passwordEditText.requestFocus()
            return
        }

        // Authenticate with Firebase
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, profile_activity::class.java)
                    startActivity(intent)
                } else {
                    // Handle Firebase exceptions
                    when (val exception = task.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            // Email not registered or invalid
                            emailEditText.error = "Email not registered or invalid"
                            emailEditText.requestFocus()
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            // Password is incorrect
                            passwordEditText.error = "Incorrect password"
                            passwordEditText.requestFocus()
                        }
                        else -> {
                            Toast.makeText(
                                context,
                                "Login failed: ${exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
    }
}
