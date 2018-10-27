package edu.gwu.findacat.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import edu.gwu.findacat.PetSearchManager
import edu.gwu.findacat.PetsAdapter
import edu.gwu.findacat.R
import edu.gwu.trivia.model.generated.petfinder.PetItem
import kotlinx.android.synthetic.main.activity_pet.*

class PetActivity : AppCompatActivity(), PetSearchManager.PetSearchCompletionListener {

    private val TAG="PetAcitivity"
    private lateinit var petSearchManager: PetSearchManager
    private lateinit var petsAdapter: PetsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet)

        petSearchManager = PetSearchManager()
        petSearchManager.petSearchCompletionListener = this
        petSearchManager.searchPets()


    }

    override fun petsLoaded(petItems: List<PetItem>) {
        petsAdapter = PetsAdapter(petItems)
        recycle_view.layoutManager = LinearLayoutManager(this)
        recycle_view.adapter = petsAdapter
    }

    override fun petsNotLoaded() {
    }

}
