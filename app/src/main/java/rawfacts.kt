/*
 suspend fun signUpWithEmailPassword(email: String, password: String, name: String): String? = withContext(Dispatchers.IO) {
       try {
           val authResult = auth.createUserWithEmailAndPassword(email, password).await()
           val user = authResult.user
           user?.sendEmailVerification()?.await()
           auth.signOut() // Sign out the user to prevent auto login
           user?.let {
               firestore.collection("user").document(it.uid).set(mapOf("name" to name)).await()
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

 suspend fun verifyEmailLink(link: String): Boolean = withContext(Dispatchers.IO) {
     try {
         val actionCodeResult = auth.checkActionCode(link).await()
         auth.applyActionCode(link).await()
         val user = auth.currentUser
         user?.reload()?.await()
         user?.isEmailVerified == true
     } catch (e: Exception) {
         Log.e("AppRepository", "Error verifying email link", e)
         false
     }
 }*/
/*
package com.ecommerceapp.data.repository

import android.util.Log
import com.ecommerceapp.models.ImageItem
import com.ecommerceapp.models.ItemModel
import com.ecommerceapp.models.SpecialOfferModel
import com.ecommerceapp.models.SpecialofferModel2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    val auth: FirebaseAuth
) {

    suspend fun fetchUserData(userId: String): Map<String, Any>? {
        return try {
            firestore.collection("user").document(userId).get().await().data
        } catch (e: Exception) {
            null
        }
    }

    suspend fun sendVerificationEmail(email: String, password: String): Boolean {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.sendEmailVerification()?.await()
            auth.signOut() // Sign out the user to prevent auto login
            true
        } catch (e: FirebaseAuthUserCollisionException) {
            Log.e("AppRepository", "Email already exists", e)
            false
        } catch (e: Exception) {
            Log.e("AppRepository", "Error sending verification email", e)
            false
        }
    }

    suspend fun signInWithEmailPassword(email: String, password: String): String? {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user
            if (user != null && user.isEmailVerified) {
                user.uid
                val userId = user.uid
                firestore.collection("user").document(userId).set(mapOf("name" to name)).await()
                userId
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Error signing in", e)
            null
        }
    }
    suspend fun signUpWithEmailPassword(email: String, password: String, name: String): String? {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            val user = auth.currentUser
            if (user != null && user.isEmailVerified) {
                val userId = user.uid
                Log.d("AppRepository", "User created with ID: $userId")

                firestore.collection("user").document(userId).set(mapOf("name" to name)).await()
                userId
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Error creating user", e)
            null
        }
    }

    suspend fun verifyEmailLink(link: String): Boolean {
        return try {
            val actionCodeResult = auth.checkActionCode(link).await()
            auth.applyActionCode(link).await()
            val user = auth.currentUser
            user?.reload()?.await()
            user?.isEmailVerified == true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun addDummyProducts(products: List<ItemModel>) {
        products.forEach { product ->
            try {
                firestore.collection("products").add(product).await()
            } catch (e: Exception) {
                Log.e("AppRepository", "Error adding product", e)
            }
        }
    }

    suspend fun getProductData(): List<ItemModel> {
        return try {
            firestore.collection("products").get().await().documents.mapNotNull { document ->
                document.toObject(ItemModel::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getImageData(): List<ImageItem> {
        return try {
            firestore.collection("images").get().await().documents.mapNotNull { document ->
                document.toObject(ImageItem::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getGrideProductData(): List<SpecialOfferModel> {
        return try {
            firestore.collection("grideproduct").get().await().documents.mapNotNull { document ->
                document.toObject(SpecialOfferModel::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getGrideProductData2(): List<SpecialofferModel2> {
        return try {
            firestore.collection("grideproduct2").get().await().documents.mapNotNull { document ->
                document.toObject(SpecialofferModel2::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}*/
// viewmodelcode
/*fun verifyEmailLink(link: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val isVerified = appRepository.verifyEmailLink(link)
                withContext(Dispatchers.Main) {
                    onResult(isVerified)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage.value = "Error verifying email link: ${e.message}"
                    onResult(false)
                }
            }
        }
    }*/