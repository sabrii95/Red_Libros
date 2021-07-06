package com.example.redlibros.match

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.redlibros.R
import kotlinx.android.synthetic.main.activity_match_sub_item.view.*

class MatchSubItem(val users: List<String>) : RecyclerView.Adapter<MatchSubItem.MatchSubItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchSubItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MatchSubItemViewHolder(layoutInflater.inflate(R.layout.activity_match_sub_item, parent, false))
    }

    override fun onBindViewHolder(holder: MatchSubItemViewHolder, position: Int) {
        holder.render(users[position])
    }

    override fun getItemCount(): Int = users.size

    class MatchSubItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun render(user: String) {
            itemView.tv_email.text = user
        }

    }
}