package com.example.redlibros.match

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.redlibros.Model.BookMatch
import com.example.redlibros.R
import kotlinx.android.synthetic.main.activity_match_item.view.*

class MatchItem(val books: List<BookMatch>) : RecyclerView.Adapter<MatchItem.MatchItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MatchItemViewHolder(layoutInflater.inflate(R.layout.activity_match_item, parent, false))
    }

    override fun onBindViewHolder(holder: MatchItemViewHolder, position: Int) {
        holder.render(books[position])

        val isExpanded = books[position].expanded
        holder.itemView.expandible.visibility = if (isExpanded) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener{
            books[position].expanded = !books[position].expanded
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = books.size

    class MatchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun render(book: BookMatch) {
            itemView.tv_item_title.text = book.title
            itemView.tv_item_authors.text = book.authors?.joinToString(", ")
            //Picasso.get().load(book.image.toString()).into(itemView.iv_image_book)

            val LayoutManager = LinearLayoutManager(itemView.rv_sub_item.context, LinearLayoutManager.VERTICAL, false)
            LayoutManager.initialPrefetchItemCount = book.users!!.size

            val matchSubItem = MatchSubItem(book.users!!)

            itemView.rv_sub_item.layoutManager = LayoutManager
            itemView.rv_sub_item.adapter = matchSubItem
        }
    }
}