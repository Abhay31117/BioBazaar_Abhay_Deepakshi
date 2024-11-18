package com.example.farmerproducts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dhaval2404.imagepicker.ImagePicker

class profile_activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var profileImage: ImageView
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var myOrders: TextView
    private lateinit var settings: TextView
    private lateinit var helpSupport: TextView
    private lateinit var logout: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        setContentView(R.layout.activity_profile)

        // Apply window insets for padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth and Storage
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        // Reference to UI elements
        profileImage = findViewById(R.id.profileImage)
        userName = findViewById(R.id.userName)
        userEmail = findViewById(R.id.userEmail)
        myOrders = findViewById(R.id.myOrders)
        settings = findViewById(R.id.settings)
        helpSupport = findViewById(R.id.helpSupport)
        logout = findViewById(R.id.logout)

        // Load user data from Firebase
        loadUserData()

        // Set click listeners for options
        setClickListeners()
    }

    private fun loadUserData() {
        // Get the current user from Firebase Authentication
        val user = auth.currentUser
        if (user != null) {
            // Set user email (from Firebase Authentication)
            userEmail.text = user.email

            // Set user name (assuming user has entered their name in Firebase)
            userName.text = user.displayName ?: "User Name"

            // Load profile image using Glide from Firebase Storage (example URL)
            val storageReference: StorageReference = storage.reference.child("profile_images/${user.uid}.jpg")
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                // Glide will load the image from the URI
                Glide.with(this)
                    .load(uri) // Firebase Storage URL for profile image
                    .into(profileImage)
            }.addOnFailureListener {
                // Handle failure (e.g., default image or error handling)
                Glide.with(this)
                    .load(R.drawable.ic_profile_placeholder) // Default profile image
                    .into(profileImage)
            }
        } else {
            // If no user is logged in, handle the case (redirect to login, show a message, etc.)
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setClickListeners() {
        // My Orders Click
        myOrders.setOnClickListener {
            // Navigate to My Orders activity or show a Toast for now
            Toast.makeText(this, "My Orders clicked", Toast.LENGTH_SHORT).show()
            // Example: startActivity(Intent(this, MyOrdersActivity::class.java))
        }

        // Settings Click
        settings.setOnClickListener {
            // Navigate to Settings activity or show a Toast for now
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
            // Example: startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Help & Support Click
        helpSupport.setOnClickListener {
            // Navigate to Help & Support activity or show a Toast for now
            Toast.makeText(this, "Help & Support clicked", Toast.LENGTH_SHORT).show()
            // Example: startActivity(Intent(this, HelpSupportActivity::class.java))
        }

        // Logout Click
        logout.setOnClickListener {
            // Handle logout process
            auth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Redirect to login screen after logout
            startActivity(Intent(this, MainLoginSignupActivity::class.java))
            finish()  // Finish the current activity to prevent back navigation
        }

        // Set the click listener for the profile image to open image picker
        profileImage.setOnClickListener {
            openImagePicker()
        }
    }

    // Open the gallery to pick an image
    private fun openImagePicker() {
        ImagePicker.with(this)
            .crop() // Optional: to crop the image
            .compress(1024) // Optional: to compress the image size
            .maxResultSize(1080, 1080) // Optional: max image resolution
            .start()
    }

    // Handle the result after the user picks an image
    private val imagePickerResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let {
                    // Upload the selected image to Firebase Storage
                    uploadImageToFirebase(it)
                }
            } else if (result.resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, "Image selection failed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    // Upload the selected image to Firebase Storage
    private fun uploadImageToFirebase(imageUri: Uri) {
        val user = auth.currentUser
        if (user != null) {
            val storageReference: StorageReference = storage.reference.child("profile_images/${user.uid}.jpg")
            storageReference.putFile(imageUri)
                .addOnSuccessListener {
                    // Get the download URL after uploading the image
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        // Use Glide to display the image in the ImageView
                        Glide.with(this)
                            .load(uri)
                            .into(profileImage)

                        // Optionally, save the URL to Firebase Database for user profile
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
