package edu.gwu.findacat.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import edu.gwu.findacat.PetSearchManager
import edu.gwu.findacat.PetsAdapter
import edu.gwu.findacat.R
import edu.gwu.trivia.model.generated.petfinder.PetItem
import kotlinx.android.synthetic.main.activity_pet_detail.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class PetDetailActivity : AppCompatActivity(), PetSearchManager.PetSearchCompletionListener {

    private lateinit var petSearchManager: PetSearchManager
    private lateinit var petsAdapter: PetsAdapter
    private lateinit var petItem: PetItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_detail)

        setSupportActionBar(pet_detail_toolbar)
        supportActionBar?.title = "Cat Details"

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_petdetailactivity, menu)

        return true
    }

    fun shareButtonPressed(item: MenuItem) {
        val sendIntent = Intent()

        sendIntent.action = Intent.ACTION_SEND

        val catName = petItem.name.t
        val catEmail = petItem.contact.email.t
        val shareText = getString(R.string.share_message, catName, catEmail)
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        sendIntent.type = "text/plain"

        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.share)))

    }
}
