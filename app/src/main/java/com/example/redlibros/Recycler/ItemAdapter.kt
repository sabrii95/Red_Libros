package com.example.redlibros.Recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.redlibros.R
import com.squareup.picasso.Picasso

class ItemAdapter(private val items: List<Item>, item: ItemClickListener): RecyclerView.Adapter<ItemAdapter.ViewHolder>(), View.OnClickListener {
    private val clickListener = item




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  this.items.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.elemento.text = items[position].name
        Picasso.get()
            .load(items[position].url)
            .error(R.drawable.missingbook)
            .placeholder(R.drawable.missingbook)
            .into(holder.img)

        holder.cardItem.setOnClickListener{clickListener.onItemClick(items[position])}
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView =view.findViewById(R.id.posterImageView)
        val elemento: TextView = view.findViewById(R.id.nameTextView2)
        val cardItem: CardView = view.findViewById(R.id.cardItem)

    }
    interface ItemClickListener {
        fun onItemClick(element:Item)
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        if (v != null) {
            v.findViewById<TextView>(R.id.nameTextView2).text = "not found"
        }
    }


}