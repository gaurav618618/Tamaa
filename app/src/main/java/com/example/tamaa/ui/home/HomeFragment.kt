package com.example.tamaa.ui.home

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
import com.example.tamaa.ui.Quote
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.myquote_list.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    val db = FirebaseFirestore.getInstance()

    val chaptersList: ArrayList<String> = ArrayList()
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerViewer = root.findViewById<RecyclerView>(R.id.rvContacts)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })
        getPosts()
        chaptersList.add("Android MVP Introduction")
        chaptersList.add("Aloha2")
        chaptersList.add("Aloha3")


        var rvChapterList2 = root.findViewById<RecyclerView>(R.id.rvContacts)
        layoutManager = LinearLayoutManager(activity)
        rvChapterList2.layoutManager = layoutManager
        rvChapterList2.adapter = ChapterAdapter(activity as MainActivity, chaptersList)


        return root


    }

    private fun getPosts(){
        db.collection("Posts")
            .get()
            .addOnSuccessListener { result ->
                System.out.println("Success")
                //for (document in result) {
                //}
            }
            .addOnFailureListener {exception ->
                System.out.println(exception)
            }
    }

}