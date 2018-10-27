package edu.gwu.findacat.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import edu.gwu.findacat.CatFactFetcher
import edu.gwu.findacat.R
import kotlinx.android.synthetic.main.activity_menu.*
import org.jetbrains.anko.activityUiThread
import org.jetbrains.anko.doAsync

class MenuActivity : AppCompatActivity(), CatFactFetcher.FactFetchedCompletionListener {
    private val TAG = "MenuActivity"
    private lateinit var catFactFetcher: CatFactFetcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        catFactFetcher = CatFactFetcher(this, textView = text_view)
        catFactFetcher.factFetchedCompletionListener = this
        catFactFetcher.fetchFact()


        find_a_cat_button.setOnClickListener {
            Log.d(TAG, "Find a cat button tapped")

            loadCatData()
        }
    }

    private fun loadCatData() {
        doAsync {
            activityUiThread {
                val intent = Intent(this@MenuActivity, PetActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun factFetched(fact: String?) {
        text_view.text = fact
    }

    override fun factNotFetched() {

    }
}
