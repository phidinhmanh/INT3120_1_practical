package com.example.unit5_amphibians.model

@kotlinx.serialization.Serializable
data class Amphibians(
    val name: String,
    val type: String,
    val description: String,
    @kotlinx.serialization.SerialName("img_src")
     val imgSrc: String
)