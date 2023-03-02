package com.example.testkinoapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

class Adapter(Items: ArrayList<Item>, val listener: Listener, var clickOnItem: ChangeFragment): RecyclerView.Adapter<Adapter.ViewHolder>(){

    var Items = Items
    var SavedItems : ArrayList<Item> = Items.clone() as ArrayList<Item>
    var Selected_Genres: ArrayList<Boolean> =
        arrayListOf(false,false,false,false,false,false,
                    false,false,false,false,false,false,
                    false, false, false)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var itemView: View? = null

        when(viewType){

            0 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.genres,parent, false)

            1 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_genre,parent, false)

            2 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_film,parent, false)
        }
        return ViewHolder(itemView!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when(holder.itemViewType){
            0 -> {
                var genre: Genre = Items[position] as Genre
                holder.textView?.text = genre.getTitle()
            }
            1 -> {
                holder.textView?.setBackgroundColor(Color.TRANSPARENT)
                var genre: Genre = Items[position] as Genre
                holder.textView?.text = genre.getTitle()
                holder.bind(listener)
                if (Selected_Genres[position]){
                    holder.textView?.setBackgroundColor(Color.parseColor("#D7F6F8"))
                }
                else
                    holder.textView?.setBackgroundColor(Color.TRANSPARENT)

            }
            2 -> {
                var film: Film = Items[position] as Film
                holder.localized_name?.text = film.getName()

                holder.imageView?.setOnClickListener {
                    clickOnItem.changeFragment(film.localized_name,
                                               film.foreignName,
                                               film.year,
                                               film.rating,
                                               film.image_url,
                                               film.description,
                                               film.genres)
                }

                val transformation: Transformation = RoundedTransformationBuilder()
                    .cornerRadiusDp(4f)
                    .build()

                Picasso.get().load(film.getUrl()).resize(145,216).error(R.drawable.ic_baseline_collections_240).transform(transformation).into(holder.imageView)

            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var textView: TextView? = null
        var imageView: ImageView? = null
        var localized_name: TextView? = null


        init {
            textView = itemView.findViewById(R.id.list_item_genre)
            imageView = itemView.findViewById(R.id.image_film)
            localized_name = itemView.findViewById(R.id.localized_name)
        }

        fun bind(listener: Adapter.Listener){
            textView?.setOnClickListener{

                if (Selected_Genres[position]){
                    for (i in 0 until Selected_Genres.size){
                        Selected_Genres[i] = false
                    }
                    Selected_Genres[position] = false
                    textView?.setBackgroundColor(Color.TRANSPARENT)
                    RemoveFilms()
                    FillBack()
                    notifyDataSetChanged()
                }
                else {
                    for (i in 0 until Selected_Genres.size){
                        Selected_Genres[i] = false
                    }
                    Selected_Genres[position] = true
                    textView?.setBackgroundColor(Color.parseColor("#D7F6F8"))
                    RemoveFilms()
                    FillFiltered(textView?.text.toString().substring(0,1).toLowerCase() + textView?.text.toString().substring(1))
                    notifyDataSetChanged()
                }

            }
        }
    }

    fun RemoveFilms(){
        if(Items.size == 15){
            return
        }
        else{
            for (i in 15 until Items.size) {
                Items.removeAt(15)
            }
        }
    }

    fun FillBack(){
        var one_film: Film

        if (Items.size == 15){
            for (i in 15 until 32){

                one_film = SavedItems[i] as Film

                Items.add(one_film)
            }
        }
    }

    fun FillFiltered( filter: String){
        var one_film: Film

        if (Items.size == 15){
            for (i in 15 until 32){

                one_film = SavedItems[i] as Film

                if (one_film.genres.contains(filter)){
                    Items.add(one_film)
                }
            }
        }
    }

    override fun getItemCount(): Int =  Items.size

    override fun getItemViewType(position: Int): Int {
        val type: Int = Items.get(position).getItemType()
        return type
    }

    interface Listener{
        fun onClickInfo()
    }
}