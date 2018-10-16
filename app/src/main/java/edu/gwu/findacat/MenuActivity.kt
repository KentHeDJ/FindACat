package edu.gwu.findacat

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_menu.*
import org.jetbrains.anko.activityUiThread
import org.jetbrains.anko.doAsync

class MenuActivity : AppCompatActivity() {
    private val TAG = "MenuActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

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
}
