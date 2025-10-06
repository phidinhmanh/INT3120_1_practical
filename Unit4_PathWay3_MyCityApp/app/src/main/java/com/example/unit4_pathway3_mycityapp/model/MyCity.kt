package com.example.unit4_pathway3_mycityapp.model

/**
 * Represents a major category of attractions or services in the city.
 */
data class Category(
    val name: String,
    val places: List<Place>
)

/**
 * Represents a specific location or point of interest within a category.
 */
data class Place(
    val name: String,
    // Note: To handle cases where data might be missing, consider making 'image' and 'description' nullable,
    // or providing default values if empty. For this example, we provide defaults.
    val image: String = "",
    val description: String = "Không có mô tả chi tiết."
)


/**
 * The root data model for the entire city application.
 */
data class MyCity(
    val name: String,
    val description: String,
    val categories: List<Category>
)