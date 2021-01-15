package com.wolf.notescout.ui.dashboard

import android.annotation.SuppressLint
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
    var onCheckBoxClick: ((NoteRestData.NoteData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NoteItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NoteItemViewHolder) {
            holder.itemView.tv_note_item.text = items[position].item
            holder.itemView.tv_user_note.text = "By: ${items[position].username}"
            if(items[position].isChecked == 1){
                holder.itemView.cb_isChecked.isChecked = true
            }
            holder.itemView.tv_note_id.text = items[position].id.toString()
            holder.itemView.tv_note_group_id.text = items[position].groupID.toString()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeAt(position: Int) {
        onItemClick?.invoke(items[position])
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class NoteItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {

            itemView.setOnClickListener {
                onItemClick?.invoke(items[layoutPosition])
            }
            itemView.cb_isChecked.setOnClickListener {

                if (itemView.cb_isChecked.isChecked) {
                    onCheckBoxClick?.invoke(items[layoutPosition].apply { isChecked = 1 })
                } else {
                    onCheckBoxClick?.invoke(items[layoutPosition].apply { isChecked = 0 })
                }
            }
        }
    }
}