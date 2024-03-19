package com.league.leaguestats.ui.rank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.league.leaguestats.R

class RankFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_rank, container, false)

        val rankViewModel = ViewModelProvider(this)[RankViewModel::class.java]

        val textView: TextView = root.findViewById(R.id.text_rank)
        rankViewModel.titleText.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
}
