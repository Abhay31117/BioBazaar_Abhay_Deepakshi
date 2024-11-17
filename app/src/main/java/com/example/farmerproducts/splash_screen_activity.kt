package com.example.farmerproducts

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@Suppress("DEPRECATION")
class splash_screen_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen) // Your splash screen layout

        Handler().postDelayed({
          val intent = Intent(this,MainLoginSignupActivity::class.java)
            startActivity(intent)
            finish()

        },3000)
    }
}