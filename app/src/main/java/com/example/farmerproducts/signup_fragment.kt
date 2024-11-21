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

class signup_fragment : Fragment() {

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance() // Initialize Firebase Authentication
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup_fragment, container, false)

        // Initialize UI elements
        usernameEditText = view.findViewById(R.id.usernameedittext)
        emailEditText = view.findViewById(R.id.emailedittext)
        passwordEditText = view.findViewById(R.id.passwordedittext)
        confirmPasswordEditText = view.findViewById(R.id.confirmpasswordedittext)
        signUpButton = view.findViewById(R.id.signupbtn)

        // Set up button click listener
        signUpButton.setOnClickListener {
            registerUser()

        }

        return view
    }

    private fun registerUser() {
        val username = usernameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        if (username.isEmpty()) {
            usernameEditText.error = "Username is required"
            usernameEditText.requestFocus()
            return
        }

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

        if (password.length < 6) {
            passwordEditText.error = "Password should be at least 6 characters"
            passwordEditText.requestFocus()
            return
        }

        if (password != confirmPassword) {
            confirmPasswordEditText.error = "Passwords do not match"
            confirmPasswordEditText.requestFocus()
            return
        }

        // Create a new user with Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Clear all fields on success
                    clearFields()
                    Toast.makeText(activity, "User registered successfully", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    // If registration fails, display a message to the user.
                    task.exception?.message?.let { message ->
                        Toast.makeText(activity, "Error: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun clearFields() {
        usernameEditText.text.clear()
        emailEditText.text.clear()
        passwordEditText.text.clear()
        confirmPasswordEditText.text.clear()
    }
}
