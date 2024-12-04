package com.example.ecommerceapp.features.detailsproduct.screen

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.detailsproduct.data.AddReviewResponse
import com.example.ecommerceapp.features.detailsproduct.data.ReviewRepository
import com.example.ecommerceapp.features.detailsproduct.domain.Review

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) :ViewModel(){

    private var isReviewsCollected = mutableStateOf(false)

     var counter = mutableIntStateOf(0)
    private var _addReviewsState = MutableSharedFlow<Resource<AddReviewResponse?>>()
    val addReviewStates: SharedFlow<Resource<AddReviewResponse?>> = _addReviewsState.asSharedFlow()
     var comment = mutableStateOf("")
    var rate = mutableFloatStateOf(0f)
    private val _selectedImages = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImages: StateFlow<List<Uri>> = _selectedImages.asStateFlow()

    fun updateSelectedImages(images: List<Uri>) {
        _selectedImages.value += images
    }
    fun updateRate(newRate: Float) {
        rate.floatValue = newRate
    }
    fun updateComment(comment:String){
        this.comment.value = comment
    }
    fun checkReviewData():Boolean{
        return comment.value.isNotEmpty() && rate.floatValue > 0
    }
    fun clearReviewData(){
        comment.value = ""
        rate.floatValue = 0f
        _selectedImages.value = emptyList()
    }
     fun addReview(review: Review) {
             counter.intValue++
         Log.d("ReviewsViewModel","counter: ${counter.intValue}")
             viewModelScope.launch(Dispatchers.IO) {

                 val response = reviewRepository.addReview(review)

                 response.collect{ state->
                     Log.d("ReviewsViewModel", "state: $state.")
                     when(state){
                         is Resource.Error -> {
                             _addReviewsState.emit(
                                 Resource.Error(
                                 exception = state.exception,
                                 errorResponse = state.errorResponse

                             ))
                         }
                         is Resource.Loading -> {
                             _addReviewsState.emit(Resource.Loading())
                         }
                         is Resource.Success -> {
                             _addReviewsState.emit(Resource.Success(state.data))
                         }

                         is Resource.Idle -> _addReviewsState.emit(Resource.Idle())
                     }
                 }
             }


     }


    private var _reviewsStates = MutableSharedFlow<Resource<List<Review>?>>()
   val reviewsStates: SharedFlow<Resource<List<Review>?>> = _reviewsStates.asSharedFlow()

    fun getReviewsByProductId(productId:String) = viewModelScope.launch(Dispatchers.IO) {
        if(!isReviewsCollected.value){
            val response = reviewRepository.getReviewsById(productId)

            response.collect{ state->
                when(state){
                    is Resource.Error -> {
                        _reviewsStates.emit(
                            Resource.Error(
                            exception = state.exception,
                            errorResponse = state.errorResponse

                        ))
                    }
                    is Resource.Loading -> {
                        _reviewsStates.emit(Resource.Loading())
                    }
                    is Resource.Success -> {
                        isReviewsCollected.value =true
                        _reviewsStates.emit(Resource.Success(state.data))
                    }

                    is Resource.Idle -> TODO()
                }
            }
        }else{
            Log.d("ReviewsViewModel","Data has been Collected")
        }

    }
    fun resetReviewsCollection() {
        isReviewsCollected.value = false
    }

    override fun onCleared() {

        isReviewsCollected.value= false
        super.onCleared()
    }

}