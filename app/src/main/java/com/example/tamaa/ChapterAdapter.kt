package com.example.tamaa

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chapter_item.view.*

class ChapterAdapter(private val context: Activity, private val chaptersList: ArrayList<String>) :
    RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.chapter_item, parent, false))
    }

    override fun getItemCount(): Int {
        return chaptersList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.chapterName?.text = chaptersList.get(position)
        holder.itemView.setOnClickListener {
            Toast.makeText(context, chaptersList.get(position), Toast.LENGTH_LONG).show()
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chapterName = view.tvChapterName
    }
}