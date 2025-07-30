package com.example.celestia.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//This class creates the Retrofit API service as a singleton.
// It allows Retrofit calls to be made from anywhere in the application.
object RetrofitInstance {

    private const val BASE_URL =  "https://api.nasa.gov/"

    val api: ApodApiService by lazy{  //api:The active state of the ApodApiService interface
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) //Automatically converts JSON format data to ApodDataModel
            .build()
            .create(ApodApiService::class.java) //Converts the ApodApiService interface into a real class.
    }

    // Hugging Face AI Chatbot API
    private const val CHAT_BASE_URL = "https://gzmoz-celestia-ai.hf.space/"

    val chatApi: ChatApiService by lazy { //chaApi: allows you to send requests to Hugging Face.
        Retrofit.Builder()
            .baseUrl(CHAT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatApiService::class.java)
    }

    //Spaceflight News
    private const val NEWS_BASE_URL = "https://api.spaceflightnewsapi.net/v4/"

    val newsApi: SpaceflightApiService by lazy {
        Retrofit.Builder()
            .baseUrl(NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpaceflightApiService::class.java)
    }
}

/*object is used to define a singleton class.
* Singleton: Only one object is created.There is only one instance in memory.
It is accessed the same way from everywhere.*/

/*by lazy?
*It is created when the api variable is used for the first time.
That is, it is created when the api.getApod() call is actually made, not when the application starts.*/