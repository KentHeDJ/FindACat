package edu.gwu.findacat.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import edu.gwu.findacat.PetSearchManager
import edu.gwu.findacat.PetsAdapter
import edu.gwu.findacat.R
import edu.gwu.trivia.model.generated.petfinder.PetItem
import kotlinx.android.synthetic.main.activity_pet.*

class PetActivity : AppCompatActivity(), PetSearchManager.PetSearchCompletionListener, PetsAdapter.OnItemClickListener {

    private val TAG="PetAcitivity"
    private lateinit var petSearchManager: PetSearchManager
    private lateinit var petsAdapter: PetsAdapter

    companion object {
        val CAT_DATA_KEY = "catData"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet)

        //setSupportActionBar(menu_)

        petSearchManager = PetSearchManager()
        petSearchManager.petSearchCompletionListener = this
        petSearchManager.searchPets()


    }

    override fun petsLoaded(petItems: List<PetItem>) {
        petsAdapter = PetsAdapter(petItems, this)
        recycle_view.layoutManager = LinearLayoutManager(this)
        recycle_view.adapter = petsAdapter
    }

    override fun petsNotLoaded() {
    }

    override fun onItemClick(petItem: PetItem, catImageView: View) {
        val detailIntent = Intent(this@PetActivity, PetDetailActivity::class.java)
        detailIntent.putExtra(CAT_DATA_KEY, petItem)
        startActivity(detailIntent)
    }
}
