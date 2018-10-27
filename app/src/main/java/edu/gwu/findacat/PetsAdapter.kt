package edu.gwu.findacat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import edu.gwu.trivia.model.generated.petfinder.PetItem


class PetsAdapter(private val petItem: List<PetItem>, private val clickListener: OnItemClickListener):
        RecyclerView.Adapter<PetsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup?.context)

        return  ViewHolder(layoutInflater.inflate(R.layout.row_cat, viewGroup, false))
    }

    override fun getItemCount(): Int {
        return petItem.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        (viewHolder as ViewHolder).bind(petItem[position], clickListener)
        val photos = petItem.get(position)

        viewHolder.bind(photos, clickListener)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val catImageView: ImageView = view.findViewById(R.id.cat_imageview)
        private val catTextView: TextView = view.findViewById(R.id.cat_textview)

        fun bind(petItem: PetItem, listener: OnItemClickListener) = with(catImageView) {
            val url = petItem.media.photos.photo[0].t
            Picasso.get().load(url).into(catImageView)

            catTextView.text = petItem.name.t.toString()
            setOnClickListener {
                listener.onItemClick(petItem, it)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(petItem: PetItem, catImageView: View)
    }
}