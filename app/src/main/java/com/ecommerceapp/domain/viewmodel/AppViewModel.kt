package com.ecommerceapp.domain.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ecommerceapp.data.repository.AppRepository
import com.ecommerceapp.models.ImageItem
import com.ecommerceapp.models.ItemModel
import com.ecommerceapp.models.SpecialOfferModel
import com.ecommerceapp.models.SpecialofferModel2
import com.ecommerceapp.models.UserData
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appRepository: AppRepository,
    application: Application
) : AndroidViewModel(application) {


    val userData = MutableLiveData<UserData?>()
    val errorMessage = MutableLiveData<String>()
    val successMessage = MutableLiveData<String>()
    val products = MutableLiveData<List<ItemModel>>()
    val images = MutableLiveData<List<ImageItem>>()
    val gridProducts = MutableLiveData<List<SpecialOfferModel>>()
    val gridProducts2 = MutableLiveData<List<SpecialofferModel2>>()
    val isLoading = MutableLiveData<Boolean>()

    private fun handleAuthException(e: Exception) {
        val message = when (e) {
            is FirebaseAuthUserCollisionException -> "User already exists. Please use a different email."
            else -> "An error occurred: ${e.message}"
        }
        errorMessage.value = message
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

    fun sendVerificationEmail(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = appRepository.sendVerificationEmail(email, password)
                withContext(Dispatchers.Main) {
                    onResult(result)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    handleAuthException(e)
                    onResult(false)
                }
            }
        }
    }
    fun signUpWithEmailPassword(email: String, password: String, name: String, onResult: (String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = appRepository.signUpWithEmailPassword(email, password, name)
                withContext(Dispatchers.Main) {
                    if (userId != null) {
                        onResult(userId)
                    } else {
                        errorMessage.value = "Error occurred during sign-up. Please try again."
                        onResult(null)
                    }
                }
            } catch (e: FirebaseAuthUserCollisionException) {
                withContext(Dispatchers.Main) {
                    errorMessage.value = "This email is already registered. Please log in."
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
                val userId = appRepository.signInWithEmailPassword(email, password)
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
        val userId = appRepository.auth.currentUser?.uid
        if (userId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                isLoading.postValue(true)
                try {
                    val data = appRepository.fetchUserData(userId)
                    withContext(Dispatchers.Main) {
                        userData.value = data
                        if (data == null) {
                            errorMessage.value = "User data not found"
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        errorMessage.value = "Error fetching user data: ${e.message}"
                    }
                } finally {
                    isLoading.postValue(false)
                }
            }
        } else {
            errorMessage.value = "User is not authenticated"
        }
    }


    fun addDummyProductsAndFetch() {
        viewModelScope.launch(Dispatchers.IO) {
            val products = listOf(
                ItemModel("Product 1", "https://upload.wikimedia.org/wikipedia/commons/5/52/Flag_of_%C3%85land.svg", 0.0, 10.0),
                ItemModel("Product 2", "https://upload.wikimedia.org/wikipedia/commons/7/77/Flag_of_Algeria.svg", 0.0, 20.0),
                ItemModel("Product 3", "https://upload.wikimedia.org/wikipedia/commons/7/77/Flag_of_Algeria.svg", 0.0, 30.0)
            )

            try {
                appRepository.addDummyProducts(products)
                Log.d("Firestore", "Dummy products added successfully!")
            } catch (e: Exception) {
                Log.w("Firestore", "Error adding dummy products: ${e.message}", e)
            }
        }
    }

   /* private fun  fetchDataFromRepository(
        fetchData: suspend () -> List<*>,
        onSuccess: (List<*>) -> Unit,
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
    }*/

    fun getProductData() {
        fetchDataFromRepository(
            fetchData = { appRepository.getProductData() },
            onSuccess = { result -> products.value = result },
            onError = { message -> errorMessage.value = message }
        )
    }

    fun getImageData() {
        fetchDataFromRepository(
            fetchData = { appRepository.getImageData() },
            onSuccess = { result -> images.value = result },
            onError = { message -> errorMessage.value = message }
        )
    }

    fun getGridProductData() {
        fetchDataFromRepository(
            fetchData = { appRepository.getGrideProductData() },
            onSuccess = { result -> gridProducts.value = result },
            onError = { message -> errorMessage.value = message }
        )
    }

    fun getGridProductData2() {
        fetchDataFromRepository(
            fetchData = { appRepository.getGrideProductData2() },
            onSuccess = { result -> gridProducts2.value = result },
            onError = { message -> errorMessage.value = message }
        )
    }
    fun addUserToFirestore(userId: String, name: String, profileImageUrl: String?, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            try {
                val result = appRepository.addUserToFirestore(userId, name, profileImageUrl)
                withContext(Dispatchers.Main) {
                    onResult(result)
                    if (result) {
                        successMessage.value = "User data saved successfully"
                    } else {
                        errorMessage.value = "Failed to save user data"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage.value = "Error saving user data: ${e.message}"
                }
            } finally {
                isLoading.postValue(false)
            }
        }
    }

}
