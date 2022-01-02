package com.example.courseworkpcversion.models

import java.io.Serializable
////a basic review constructor
data class Review(val id: String = "", val restaurantName: String = "", val rating: Float = 0F,
             val reviewText: String = "", val userId: String = "", val image: String = ""): Serializable