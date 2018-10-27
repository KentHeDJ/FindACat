package edu.gwu.findacat.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Picasso
import edu.gwu.findacat.PetSearchManager
import edu.gwu.findacat.PetsAdapter
import edu.gwu.findacat.R
import edu.gwu.trivia.model.generated.petfinder.PetItem
import kotlinx.android.synthetic.main.activity_pet_detail.*

class PetDetailActivity : AppCompatActivity(), PetSearchManager.PetSearchCompletionListener {

    private lateinit var petSearchManager: PetSearchManager
    private lateinit var petsAdapter: PetsAdapter
    private lateinit var petItem: PetItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_detail)

        petSearchManager = PetSearchManager()
        petSearchManager.petSearchCompletionListener = this
        petSearchManager.searchPets()

        petItem = intent.getParcelableExtra<PetItem>(PetActivity.CAT_DATA_KEY)
        val url = petItem.media.photos.photo[0].t
        Picasso.get().load(url).into(detail_imageview)
        name_textview.text = petItem.name.t
        gender_textview.text = petItem.sex.t
        description_textview.text = petItem.description?.t

    }

    override fun petsLoaded(petItems: List<PetItem>) {

    }

    override fun petsNotLoaded() {
    }
}
