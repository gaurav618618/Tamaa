package com.example.tamaa.ui.rankings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tamaa.ChapterAdapter
import com.example.tamaa.MainActivity
import com.example.tamaa.R
import com.google.firebase.firestore.FirebaseFirestore

class RankingsFragment : Fragment() {

    private lateinit var notificationsViewModel: RankingsViewModel

    private lateinit var layoutManager: RecyclerView.LayoutManager
    val db = FirebaseFirestore.getInstance()
    val chaptersList: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(RankingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)

        notificationsViewModel.text.observe(this, Observer {
            textView.text = it
        })

        var rvChapterList2 = root.findViewById<RecyclerView>(R.id.rvRankings)

        db.collection("Posts").get().addOnSuccessListener { result ->
            for (document in result) {
                chaptersList.add(document.data["name"].toString())
            }
            layoutManager = LinearLayoutManager(activity)
            rvChapterList2.layoutManager = layoutManager
            rvChapterList2.adapter = ChapterAdapter(activity as MainActivity, chaptersList)
        }

        return root
    }
}