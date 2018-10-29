package edu.gwu.findacat.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import edu.gwu.findacat.PersistenceManager
import edu.gwu.findacat.PetSearchManager
import edu.gwu.findacat.PetsAdapter
import edu.gwu.findacat.R
import edu.gwu.findacat.R.string.email
import edu.gwu.trivia.model.generated.petfinder.PetItem
import kotlinx.android.synthetic.main.activity_pet_detail.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class PetDetailActivity : AppCompatActivity(), PetSearchManager.PetSearchCompletionListener {

    private lateinit var petSearchManager: PetSearchManager
    private lateinit var petsAdapter: PetsAdapter
    private lateinit var petItem: PetItem
    private lateinit var persistenceManager: PersistenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_detail)

        setSupportActionBar(pet_detail_toolbar)
        supportActionBar?.title = "Cat Details"

        persistenceManager = PersistenceManager(this)

        petSearchManager = PetSearchManager()
        petSearchManager.petSearchCompletionListener = this
        petSearchManager.searchPets(90120)

        petItem = intent.getParcelableExtra<PetItem>(PetActivity.CAT_DATA_KEY)
        val url = petItem.media.photos.photo[0].t
        Picasso.get().load(url).into(detail_imageview)
        name_textview.text = "Name:    " + petItem.name.t
        gender_textview.text = "Gender:    " + petItem.sex.t
        zip_textview.text = "Zip:    " + petItem.contact.zip.t
        description_textview.text = "Description:    " + petItem.description?.t


    }

    override fun petsLoaded(petItems: List<PetItem>) {

    }

    override fun petsNotLoaded() {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_petdetailactivity, menu)

        return true
    }

    fun favoriteButtonPressed(item: MenuItem) {
        persistenceManager.saveFavorites(petItem)
    }

    fun emailButtonPressed(item: MenuItem) {
        val receiverEmail = petItem.contact.email.t
        val catName = petItem.name.t
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.setData(Uri.parse("mailto:${receiverEmail}"))

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "I am interested in your cat named ${catName}")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Can I have more info of your cat? Thank you.")

        startActivity(Intent.createChooser(emailIntent, resources.getText(R.string.sent_email)))

    }

    fun shareButtonPressed(item: MenuItem) {
        val sendIntent = Intent()

        sendIntent.action = Intent.ACTION_SEND

        val catName = petItem.name.t
        val catEmail = petItem.contact.email.t
        val catPhoto = petItem.media.photos.photo[0].t
        val shareText = getString(R.string.share_message, catName, catEmail, catPhoto)
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        sendIntent.type = "text/plain"

        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.share)))

    }
}
