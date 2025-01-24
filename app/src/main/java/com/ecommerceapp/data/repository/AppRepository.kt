package com.ecommerceapp.data.repository


import android.util.Log
import com.ecommerceapp.models.CategoriesModel
import com.ecommerceapp.models.ImageItemModel
import com.ecommerceapp.models.ItemModel
import com.ecommerceapp.models.UserDataModel
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
            private const val TRENDING_PRODUCT_COLLECTION = "trendingproduct"
            private const val SPECIAL_TRENDING_PRODUCT_COLLECTION = "specialtrendingproduct"
            private const val SPECIAL_TRENDING_PRODUCT_COLLECTION_UP = "specialtrendingproductup"
            private const val SPECIAL_TRENDING_PRODUCT_COLLECTION_DOWN = "gridProductsdown"
            private const val CHILDERN_PRODUCT_COLLECTION = "childernproduct"
            private const val CHILDERN_PRODUCT_CENTER_COLLECTION = "childernproductcenter"
            private const val CHILDERN_PRODUCT_CENTER_COLLECTION_UP = "childernproductcenterup"
            private const val SPECIAL_CHILDERN_PRODUCT_COLLECTION = "specialchildernproduct"
            private const val CHILDERN_PRODUCT_CENTER_COLLECTION_DOWN = "childernproductcenterdown"

            private const val RECOMMENDED_PRODUCT_COLLECTION = "recommendedproduct"
            private const val RECOMMENDED_PRODUCT_CENTER_COLLECTION = "recommendedproductcenter"
            private const val RECOMMENDED_PRODUCT_CENTER_COLLECTION_UP = "recommendedproductcenterup"
            private const val RECOMMENDED_PRODUCT_CENTER_COLLECTION_DOWN = "recommendedproductcenterdown"
            private const val CATOGORY_PRODUCT_CENTER_COLLECTION = "categoryproductcenter"

        }

        suspend fun fetchUserData(userId: String): UserDataModel? = withContext(Dispatchers.IO) {
            try {
                val document = firestore.collection(USER_COLLECTION).document(userId).get().await()
                document.toObject(UserDataModel::class.java)
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

        suspend fun getImageData(): List<ImageItemModel> = withContext(Dispatchers.IO) {
            try {
                firestore.collection(IMAGES_COLLECTION).get().await().documents.mapNotNull { document ->
                    document.toObject(ImageItemModel::class.java)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching image data", e)
                emptyList()
            }
        }
        suspend fun getTrendingpProductData(): List<ItemModel> = withContext(Dispatchers.IO) {
            try {
                firestore.collection(TRENDING_PRODUCT_COLLECTION).get().await().documents.mapNotNull { document ->
                    document.toObject(ItemModel::class.java)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching image data", e)
                emptyList()
            }
        }

        suspend fun getAccessoriesProductData(): List<CategoriesModel> = withContext(Dispatchers.IO) {
            try {
                firestore.collection(CATOGORY_PRODUCT_CENTER_COLLECTION).get().await().documents.mapNotNull { document ->
                    document.toObject(CategoriesModel::class.java)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching image data", e)
                emptyList()
            }
        }
        suspend fun getChildernProductData(): List<ImageItemModel> = withContext(Dispatchers.IO) {
            try {
                firestore.collection(CHILDERN_PRODUCT_COLLECTION).get().await().documents.mapNotNull { document ->
                    document.toObject(ImageItemModel::class.java)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching image data", e)
                emptyList()
            }
        }
        suspend fun getChildernCenterProductData(): List<ItemModel> = withContext(Dispatchers.IO) {
            try {
                firestore.collection(CHILDERN_PRODUCT_CENTER_COLLECTION).get().await().documents.mapNotNull { document ->
                    document.toObject(ItemModel::class.java)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching image data", e)
                emptyList()
            }
        }
        suspend fun getChildernCenterProductDataUp(): List<ItemModel> = withContext(Dispatchers.IO) {
            try {
                firestore.collection(CHILDERN_PRODUCT_CENTER_COLLECTION_UP).get().await().documents.mapNotNull { document ->
                    document.toObject(ItemModel::class.java)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching image data", e)
                emptyList()
            }
        }
        suspend fun getChildernCenterProductDataDown(): List<ItemModel> = withContext(Dispatchers.IO) {
            try {
                firestore.collection(CHILDERN_PRODUCT_CENTER_COLLECTION_DOWN).get().await().documents.mapNotNull { document ->
                    document.toObject(ItemModel::class.java)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching image data", e)
                emptyList()
            }
        }


        //for new line
        suspend fun getRecommendedProductData(): List<ImageItemModel> = withContext(Dispatchers.IO) {
            try {
                firestore.collection(RECOMMENDED_PRODUCT_COLLECTION).get().await().documents.mapNotNull { document ->
                    document.toObject(ImageItemModel::class.java)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching image data", e)
                emptyList()
            }
        }
<<<<<<< HEAD
        suspend fun getRecommendedCenterProductData(): List<ItemModel> = withContext(Dispatchers.IO) {
            try {
                firestore.collection(RECOMMENDED_PRODUCT_CENTER_COLLECTION).get().await().documents.mapNotNull { document ->
                    document.toObject(ItemModel::class.java)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching image data", e)
                emptyList()
            }
        }
        suspend fun getRecommendedProductDataUp(): List<ItemModel> = withContext(Dispatchers.IO) {
            try {
                firestore.collection(RECOMMENDED_PRODUCT_CENTER_COLLECTION_UP).get().await().documents.mapNotNull { document ->
                    document.toObject(ItemModel::class.java)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching image data", e)
                emptyList()
            }
        }
        suspend fun getRecommendedCenterProductDataDown(): List<ItemModel> = withContext(Dispatchers.IO) {
            try {
                firestore.collection(RECOMMENDED_PRODUCT_CENTER_COLLECTION_DOWN).get().await().documents.mapNotNull { document ->
                    document.toObject(ItemModel::class.java)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching image data", e)
                emptyList()
            }
        }

        suspend fun getChildernOfferProductData(): List<ItemModel> = withContext(Dispatchers.IO) {
            try {
                firestore.collection(SPECIAL_CHILDERN_PRODUCT_COLLECTION).get().await().documents.mapNotNull { document ->
                    document.toObject(ItemModel::class.java)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching image data", e)
                emptyList()
            }
        }


        suspend fun getGrideProductData(): List<ItemModel> = withContext(Dispatchers.IO) {
            try {
                firestore.collection(PRODUCTS_COLLECTION).get()
                    .await().documents.mapNotNull { document ->
                        document.toObject(ItemModel::class.java)
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching grid product data", e)
                emptyList()
            }
        }


        suspend fun getGrideProductData2(): List<ItemModel> = withContext(Dispatchers.IO) {
            try {
                firestore.collection(PRODUCTS_COLLECTION).get()
                    .await().documents.mapNotNull { document ->
                        document.toObject(ItemModel::class.java)
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching grid product data 2", e)
                emptyList()
            }
        }



        suspend fun getSpecialTrendingProductData(): List<ItemModel> = withContext(Dispatchers.IO) {
            try {
                val result = firestore.collection(SPECIAL_TRENDING_PRODUCT_COLLECTION).get().await().documents.mapNotNull { document ->
                    document.toObject(ItemModel::class.java)
                }
                Log.d(TAG, "Fetched trending products: $result")
                result
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching Trending_Product", e)
                emptyList()
            }
        }
        suspend fun getProductdown(): List<ItemModel> = withContext(Dispatchers.IO) {
            try {
                val result = firestore.collection(SPECIAL_TRENDING_PRODUCT_COLLECTION_DOWN).get().await().documents.mapNotNull { document ->
                    document.toObject(ItemModel::class.java)
                }
                Log.d(TAG, "Fetched trending products: $result")
                result
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching Trending_Product", e)
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

        suspend fun getCategoryProductsByType(categoryName: String, productType: String): List<CategoriesModel> {
            val productsList = mutableListOf<CategoriesModel>()

            val productsSnapshot = firestore.collection("categories")
                .document(categoryName)
                .collection("products")
                .get()
                .await()

            productsSnapshot.forEach { productDoc ->
                val name = productDoc.getString("name") ?: ""
                val imageUrl = productDoc.getString("imageUrl") ?: ""
                val isBestSeller = productDoc.getBoolean("isBestSeller") ?: false
                val isFeatured = productDoc.getBoolean("isFeatured") ?: false
                val isDiscounted = productDoc.getBoolean("isDiscount") ?: false
                Log.d("CategoryProducts", "Product: $name, BestSeller: $isBestSeller, SpecialOffer: $isFeatured, ImageUrl: $imageUrl")
                if (productType == "specialOffer" && isFeatured) {
                    productsList.add(CategoriesModel(name, imageUrl, isBestSeller, isFeatured))
                } else if (productType == "newProduct" && isBestSeller) {
                    productsList.add(CategoriesModel(name, imageUrl, isBestSeller, isFeatured))
                }
               /* else if (productType == "discounted" && isDiscounted) {
                    productsList.add(AccessoriesModel(name, imageUrl, isBestSeller, isFeatured, isDiscounted))
                }*/
            }
            Log.d("CategoryProducts", "Total products loaded: ${productsList.size}")
            return productsList
        }

    }
=======
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


>>>>>>> 884c252fb20745c665e0fde3f029fee1f9320a35
