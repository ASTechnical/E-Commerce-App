package com.ecommerceapp.data.repository

import android.util.Log
import com.ecommerceapp.models.ImageItem
import com.ecommerceapp.models.ItemModel
import com.ecommerceapp.models.SpecialOfferModel
import com.ecommerceapp.models.SpecialofferModel2
import com.ecommerceapp.models.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    val auth: FirebaseAuth
) {

    companion object {
        private const val TAG = "AppRepository"
        private const val USER_COLLECTION = "users"
        private const val PRODUCTS_COLLECTION = "products"
        private const val IMAGES_COLLECTION = "images"
        private const val GRID_PRODUCT_COLLECTION = "grideproduct"
        private const val GRID_PRODUCT_COLLECTION2 = "grideproduct2"
    }

    suspend fun fetchUserData(userId: String): UserData? = withContext(Dispatchers.IO) {
        try {
            val document = firestore.collection(USER_COLLECTION).document(userId).get().await()
            document.toObject(UserData::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching user data", e)
            null
        }
    }

    suspend fun sendVerificationEmail(email: String, password: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                authResult.user?.sendEmailVerification()?.await()
                auth.signOut()
                true
            } catch (e: FirebaseAuthUserCollisionException) {
                Log.e(TAG, "Email already exists", e)
                false
            } catch (e: Exception) {
                Log.e(TAG, "Error sending verification email", e)
                false
            }
        }

    suspend fun signUpWithEmailPassword(email: String, password: String, name: String): String? =
        withContext(Dispatchers.IO) {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val user = authResult.user

                user?.let {
                    // First, create the Firestore document
                    firestore.collection("users").document(it.uid).set(mapOf("name" to name))
                        .await()

                    // Then, send the email verification
                    it.sendEmailVerification().await()

                    // Finally, sign out the user to prevent auto-login
                    //  auth.signOut()
                    it.uid
                }
            } catch (e: FirebaseAuthUserCollisionException) {
                Log.e("AppRepository", "Email already exists", e)
                null
            } catch (e: Exception) {
                Log.e("AppRepository", "Error creating user", e)
                null
            }
        }

    suspend fun signInWithEmailPassword(email: String, password: String): String? =
        withContext(Dispatchers.IO) {
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val user = authResult.user
                // Check if user is not null and email is verified
                if (user != null && user.isEmailVerified) {
                    val userId = user.uid

                    userId
                } else {
                    // Handle case where user is null or email is not verified
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error signing in", e)
                null
            }
        }


    suspend fun addDummyProducts(products: List<ItemModel>) = withContext(Dispatchers.IO) {
        products.forEach { product ->
            try {
                firestore.collection(PRODUCTS_COLLECTION).add(product).await()
            } catch (e: Exception) {
                Log.e(TAG, "Error adding product", e)
            }
        }
    }

    suspend fun getProductData(): List<ItemModel> = withContext(Dispatchers.IO) {
        try {
            firestore.collection(PRODUCTS_COLLECTION).get()
                .await().documents.mapNotNull { document ->
                document.toObject(ItemModel::class.java)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching product data", e)
            emptyList()
        }
    }

    suspend fun getImageData(): List<ImageItem> = withContext(Dispatchers.IO) {
        try {
            firestore.collection(IMAGES_COLLECTION).get().await().documents.mapNotNull { document ->
                document.toObject(ImageItem::class.java)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching image data", e)
            emptyList()
        }
    }

    suspend fun getGrideProductData(): List<SpecialOfferModel> = withContext(Dispatchers.IO) {
        try {
            firestore.collection(GRID_PRODUCT_COLLECTION).get()
                .await().documents.mapNotNull { document ->
                document.toObject(SpecialOfferModel::class.java)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching grid product data", e)
            emptyList()
        }
    }

    suspend fun getGrideProductData2(): List<SpecialofferModel2> = withContext(Dispatchers.IO) {
        try {
            firestore.collection(GRID_PRODUCT_COLLECTION2).get()
                .await().documents.mapNotNull { document ->
                document.toObject(SpecialofferModel2::class.java)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching grid product data 2", e)
            emptyList()
        }
    }
    suspend fun addUserToFirestore(userId: String, name: String, profileImageUrl: String?) = withContext(Dispatchers.IO) {
        try {
            val userData = mapOf(
                "name" to name,
                "profileImageUrl" to profileImageUrl
            )
            firestore.collection(USER_COLLECTION).document(userId).set(userData).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error saving user data", e)
            false
        }
    }
}


