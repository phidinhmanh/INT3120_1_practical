package com.example.marsphotos.fake

import com.example.marsphotos.model.MarsPhoto

object FakeMarsResource {
    const val idOne = "img1"
    const val idTwo = "img2"
    const val imgOne = "url.one"
    const val imgTwo = "url.two"

    val photo = listOf(
        MarsPhoto(
            id = idOne,
            imgSrc = imgOne
        ),
        MarsPhoto(
            id = idTwo,
            imgSrc = imgTwo
        )
    )
}