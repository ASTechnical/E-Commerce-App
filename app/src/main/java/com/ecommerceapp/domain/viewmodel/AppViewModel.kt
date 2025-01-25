package com.ecommerceapp.domain.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ecommerceapp.data.repository.Repository
import com.ecommerceapp.models.CategoriesModel

import com.ecommerceapp.models.ImageItemModel
import com.ecommerceapp.models.ItemModel

import com.ecommerceapp.models.UserDataModel
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {


    // New LiveData to hold category products
    val categoryProducts = MutableLiveData<List<CategoriesModel>>()

    val userDataModel = MutableLiveData<UserDataModel?>()

    private val successMessage = MutableLiveData<String>()
    val products = MutableLiveData<List<ItemModel>>()

    val recommendedproduct = MutableLiveData<List<ImageItemModel>>()



    private val _specialOffers = MutableLiveData<List<CategoriesModel>>()
    val specialOffers: LiveData<List<CategoriesModel>> get() = _specialOffers

    private val _newProducts = MutableLiveData<List<CategoriesModel>>()
    val newProducts: LiveData<List<CategoriesModel>> get() = _newProducts



    val errorMessage: LiveData<String?> get() = _errorMessage
    private val _errorMessage = MutableLiveData<String?>()

    val isLoading = MutableLiveData<Boolean>()


    private fun handleAuthException(e: Exception) {
        val message = when (e) {
            is FirebaseAuthUserCollisionException -> "User already exists. Please use a different email."
            else -> "An error occurred: ${e.message}"
        }
        _errorMessage.value = message
    }

    private fun <T> fetchDataFromRepository(
        fetchData: suspend () -> List<T>,
        onSuccess: (List<T>) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            try {
                val result = fetchData()
                withContext(Dispatchers.Main) {
                    if (result.isNotEmpty()) {
                        onSuccess(result)
                    } else {
                        onError("No data found")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error fetching data: ${e.message}")
                }
            } finally {
                isLoading.postValue(false)
            }
        }
    }


    fun signUpWithEmailPassword(
        email: String,
        password: String,
        name: String,
        onResult: (String?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = repository.signUpWithEmailPassword(email, password, name)
                withContext(Dispatchers.Main) {
                    if (userId != null) {
                        onResult(userId)
                    } else {
                        _errorMessage.value = "Error occurred during sign-up. Please try again."
                        onResult(null)
                    }
                }
            } catch (e: FirebaseAuthUserCollisionException) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "This email is already registered. Please log in."
                    onResult(null)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    handleAuthException(e)
                    onResult(null)
                }
            }
        }
    }

    fun signInWithEmailPassword(email: String, password: String, onResult: (String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = repository.signInWithEmailPassword(email, password)
                withContext(Dispatchers.Main) {
                    onResult(userId)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    handleAuthException(e)
                    onResult(null)
                }
            }
        }
    }

    fun fetchUserData() {
        val userId = repository.auth.currentUser?.uid
        if (userId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                isLoading.postValue(true)
                try {
                    val data = repository.fetchUserData(userId)
                    withContext(Dispatchers.Main) {
                        userDataModel.value = data
                        if (data == null) {
                            _errorMessage.value = "User data not found"
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _errorMessage.value = "Error fetching user data: ${e.message}"
                    }
                } finally {
                    isLoading.postValue(false)
                }
            }
        } else {
            _errorMessage.value = "User is not authenticated"
        }
    }


    fun addDummyProductsAndFetch() {
        viewModelScope.launch(Dispatchers.IO) {
            val products = listOf(
                ItemModel(
                    "Product 1",
                    "https://upload.wikimedia.org/wikipedia/commons/5/52/Flag_of_%C3%85land.svg",
                    0.0,
                    10.0
                ),
                ItemModel(
                    "Product 2",
                    "https://upload.wikimedia.org/wikipedia/commons/7/77/Flag_of_Algeria.svg",
                    0.0,
                    20.0
                ),
                ItemModel(
                    "Product 3",
                    "https://upload.wikimedia.org/wikipedia/commons/7/77/Flag_of_Algeria.svg",
                    0.0,
                    30.0
                )
            )

            try {
                repository.addDummyProducts(products)
                Log.d("Firestore", "Dummy products added successfully!")
            } catch (e: Exception) {
                Log.w("Firestore", "Error adding dummy products: ${e.message}", e)
            }
        }
    }


    fun getProductData() {
        fetchDataFromRepository(
            fetchData = { repository.getProductData() },
            onSuccess = { result -> products.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    fun getImageData() {
        fetchDataFromRepository(
            fetchData = { repository.getImageData() },
            onSuccess = { result -> recommendedproduct.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    fun getTrendingpProductData() {
        fetchDataFromRepository(
            fetchData = { repository.getTrendingpProductData() },
            onSuccess = { result -> products.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    fun getAccessoriesProductData() {
        fetchDataFromRepository(
            fetchData = { repository.getAccessoriesProductData() },
            onSuccess = { result -> categoryProducts.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    fun getGridProductData() {
        fetchDataFromRepository(
            fetchData = { repository.getGrideProductData() },
            onSuccess = { result -> products.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    fun getSpecialTrendingProductData() {
        fetchDataFromRepository(
            fetchData = { repository.getSpecialTrendingProductData() },
            onSuccess = { result -> products.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    fun getProductdown() {
        fetchDataFromRepository(
            fetchData = { repository.getProductdown() },
            onSuccess = { result -> products.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }


    fun getGridProductData2() {
        fetchDataFromRepository(
            fetchData = { repository.getGrideProductData2() },
            onSuccess = { result -> products.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    fun getChildernProductData() {
        fetchDataFromRepository(
            fetchData = { repository.getChildernProductData() },
            onSuccess = { result -> recommendedproduct.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    fun getChildernCenterProductData() {
        fetchDataFromRepository(
            fetchData = { repository.getChildernCenterProductData() },
            onSuccess = { result -> products.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    fun getChildernCenterProductDataUp() {
        fetchDataFromRepository(
            fetchData = { repository.getChildernCenterProductDataUp() },
            onSuccess = { result -> products.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    fun getChildernCenterProductDataDown() {
        fetchDataFromRepository(
            fetchData = { repository.getChildernCenterProductDataDown() },
            onSuccess = { result -> products.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    // new line
    fun getRecommendedProductData() {
        fetchDataFromRepository(
            fetchData = { repository.getRecommendedProductData() },
            onSuccess = { result -> recommendedproduct.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    fun getRecommendedCenterProductData() {
        fetchDataFromRepository(
            fetchData = { repository.getRecommendedCenterProductData() },
            onSuccess = { result -> products.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    fun getRecommendedProductDataUp() {
        fetchDataFromRepository(
            fetchData = { repository.getRecommendedProductDataUp() },
            onSuccess = { result -> products.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    fun getRecommendedCenterProductDataDown() {
        fetchDataFromRepository(
            fetchData = { repository.getRecommendedCenterProductDataDown() },
            onSuccess = { result -> products.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    fun getChildernOfferProductData() {
        fetchDataFromRepository(
            fetchData = { repository.getChildernOfferProductData() },
            onSuccess = { result -> products.value = result },
            onError = { message -> _errorMessage.value = message }
        )
    }

    fun addUserToFirestore(
        userId: String,
        name: String,
        profileImageUrl: String?,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            try {
                val result = repository.addUserToFirestore(userId, name, profileImageUrl)
                withContext(Dispatchers.Main) {
                    onResult(result)
                    if (result) {
                        successMessage.value = "User data saved successfully"
                    } else {
                        _errorMessage.value = "Failed to save user data"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Error saving user data: ${e.message}"
                }
            } finally {
                isLoading.postValue(false)
            }
        }
    }


    fun fetchCategoryProducts(categoryName: String) {
        // Fetch special offer products
        fetchDataFromRepository(
            fetchData = { repository.getCategoryProductsByType(categoryName, "specialOffer") },
            onSuccess = { products -> _specialOffers.value = products },
            onError = { error -> _errorMessage.value = error }
        )

        // Fetch new product products
        fetchDataFromRepository(
            fetchData = { repository.getCategoryProductsByType(categoryName, "newProduct") },
            onSuccess = { products -> _newProducts.value = products },
            onError = { error -> _errorMessage.value = error }
        )


    }
}

