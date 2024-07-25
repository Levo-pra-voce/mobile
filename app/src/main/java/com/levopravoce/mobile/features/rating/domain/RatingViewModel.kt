package com.levopravoce.mobile.features.rating.domain

import androidx.lifecycle.ViewModel
import com.levopravoce.mobile.features.order.data.dto.RatingDTO
import com.levopravoce.mobile.features.rating.data.RatingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val ratingRepository: RatingRepository
) : ViewModel() {
    suspend fun sendReview(orderId: Long, comment: String, rating: Int) {
        val ratingDTO = RatingDTO(
            comment = comment,
            note = rating.toDouble()
        )
        try {
            ratingRepository.sendReview(orderId, ratingDTO)
        } catch (e: Exception) {
            // handle error
        }
    }
}