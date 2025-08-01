package com.example.celestia.model

//It enables the JSON response received via Retrofit to be converted into a Kotlin object (deserialize process).
data class ApodDataModel(
  val date : String,
  val explanation : String,
  val title : String,
  val url : String
)
/*
{
    "copyright": "Massimo Di Fusco",
    "date": "2025-05-23",
    "explanation": "Most globular star clusters roam the halo of our Milky Way galaxy, but globular cluster NGC 6366 lies close to the galactic plane. About 12,000 light-years away toward the constellation Ophiuchus, the cluster's starlight is dimmed and reddened by the Milky Way's interstellar dust when viewed from planet Earth. As a result, the stars of NGC 6366 look almost golden in this telescopic scene, especially when seen next to relatively bright, bluish, and nearby star 47 Ophiuchi. Compared to the hundred thousand stars or so gravitationally bound in distant NGC 6366, 47 Oph itself is a binary star system  a mere 100 light-years away. Still, the co-orbiting stars of 47 Oph are too close together to be individually distinguished in the image.",
    "hdurl": "https://apod.nasa.gov/apod/image/2505/NGC6366_3500.jpg",
    "media_type": "image",
    "service_version": "v1",
    "title": "NGC 6366 vs 47 Ophiuchi",
    "url": "https://apod.nasa.gov/apod/image/2505/NGC6366_1024.jpg"
}*/
