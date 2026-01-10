package com.example.car_project_1.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.car_project_1.fragment.FragmentMap
import com.example.car_project_1.fragment.ListFragment
import com.example.car_project_1.callback.ScoreClickedCallBack
import com.example.car_project_1.R
import com.example.car_project_1.databinding.ActivityToptenBinding

class TopTenActivity : AppCompatActivity(), ScoreClickedCallBack {

    private lateinit var binding: ActivityToptenBinding

    private val listFragment = ListFragment()
    private val fragmentMap = FragmentMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityToptenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
        supportFragmentManager
            .beginTransaction()
            .add(R.id.layLst, listFragment)
            .add(R.id.layMap, fragmentMap)
            .commit()

        listFragment.setListener(this)
    }



    override fun onScoreClicked(lat: Double, lon: Double) {
        fragmentMap.moveToLocation(lat, lon)
    }

}