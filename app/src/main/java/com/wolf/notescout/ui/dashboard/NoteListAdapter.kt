package com.wolf.notescout.ui.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wolf.notescout.R
import com.wolf.notescout.data.model.NoteRestData
import kotlinx.android.synthetic.main.item_note.view.*

class NoteListAdapter (context: Context?, var items: ArrayList<NoteRestData.NoteData>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((NoteRestData.NoteData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NoteItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is NoteItemViewHolder) {
            holder.itemView.tv_note_item.text = items[position].item
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeAt(position: Int){
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class NoteItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[layoutPosition])
            }
        }
    }
}