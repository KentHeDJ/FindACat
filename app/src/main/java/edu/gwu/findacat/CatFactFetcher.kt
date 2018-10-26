package edu.gwu.findacat

import android.content.Context
import android.util.Log
import android.widget.TextView
import edu.gwu.findacat.model.generated_Catfact.CatFactResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

class CatFactFetcher(private val context: Context, private var textView: TextView) {

    private val TAG = "CatFactFetcher"
    var factFetchedCompletionListener: FactFetchedCompletionListener? = null

    interface FactFetchedCompletionListener {
        fun factFetched(fact: String?)
        fun factNotFetched()
    }

    interface ApiEndpointInterface {
        @GET("fact")
        fun getCatFactResponse(): Call<CatFactResponse>
    }

    fun fetchFact() {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://catfact.ninja/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val apiEndpoint = retrofit.create(ApiEndpointInterface::class.java)

        apiEndpoint.getCatFactResponse().enqueue(object: Callback<CatFactResponse> {
            override fun onFailure(call: Call<CatFactResponse>, t: Throwable) {
                Log.d(TAG, "API call failed")
            }

            override fun onResponse(call: Call<CatFactResponse>, response: Response<CatFactResponse>) {
                val catFactResponseBody = response.body()

                factFetchedCompletionListener?.factFetched(catFactResponseBody?.fact)
            }

        })


    }
}