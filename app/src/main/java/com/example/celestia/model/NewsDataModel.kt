package com.example.celestia.model

//Model class to interpret JSON response from API in Kotlin
data class NewsDataModel(
    val id : Int,
    val title: String,
    val summary: String,
    val image_url: String,
    val published_at: String,
    val url: String
)

/*
{
   "id": 31899,
    "title": "NASA Air Taxi Passenger Comfort Studies Move Forward",
    "authors": [
    {
        "name": "NASA",
        "socials": null
    }
    ],
    "url": "https://www.nasa.gov/centers-and-facilities/armstrong/nasa-air-taxi-passenger-comfort-studies-move-forward/",
    "image_url": "https://images-assets.nasa.gov/image/AFRC2024-0168-23/AFRC2024-0168-23~large.jpg",
    "news_site": "NASA",
    "summary": "NASA’s Advanced Air Mobility vision involves the skies above the U.S. filled with new types of aircraft, including air taxis. But making that vision a reality involves ensuring that people will actually want to ride these aircraft – which is why NASA has been working to evaluate comfort, to see what passengers will and won’t […]",
    "published_at": "2025-06-20T16:22:17Z",
    "updated_at": "2025-06-20T16:30:09.942244Z",
    "featured": false,
    "launches": [],
    "events": []
},*/