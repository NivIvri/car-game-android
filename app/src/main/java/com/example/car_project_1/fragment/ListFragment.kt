package com.example.car_project_1.fragment

import com.example.car_project_1.ui.ScoreAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.car_project_1.callback.ScoreClickedCallBack
import com.example.car_project_1.data.TopTenStorage
import com.example.car_project_1.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var listener: ScoreClickedCallBack? = null

    fun setListener(listener: ScoreClickedCallBack) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scores = TopTenStorage.loadScores(requireContext())

        binding.recyclerScores.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerScores.adapter =
            ScoreAdapter(scores) { score ->
                listener?.onScoreClicked(score.lat, score.lon)
            }

        if (scores.isNotEmpty()) {
            listener?.onScoreClicked(scores[0].lat, scores[0].lon)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}