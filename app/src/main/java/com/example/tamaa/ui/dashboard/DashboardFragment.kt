package com.example.tamaa.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tamaa.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val textView: TextView = root.findViewById(R.id.text_dashboard)
        val firstName: TextView = root.findViewById(R.id.user_name)
        val age: TextView = root.findViewById(R.id.age)
        val profilePicture: ImageView = root.findViewById(R.id.profile_picture)
        val foodExample0: ImageView = root.findViewById(R.id.food_example0)
        val foodExample1: ImageView = root.findViewById(R.id.food_example1)

        dashboardViewModel.text.observe(this, Observer {
            textView.text = it
            firstName.setText("Mana")
            age.setText("19")

            profilePicture.setImageResource(R.drawable.chez_mamy)
            foodExample0.setImageResource(R.drawable.firifiri)
            foodExample1.setImageResource(R.drawable.rougets)
        })
        return root
    }
}