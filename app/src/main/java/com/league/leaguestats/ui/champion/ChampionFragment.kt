package com.league.leaguestats.ui.champion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.league.leaguestats.R

class ChampionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_champion, container, false)

        val tournamentViewModel = ViewModelProvider(this)[ChampionViewModel::class.java]

        val textView: TextView = root.findViewById(R.id.text_champion)
        tournamentViewModel.titleText.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
}
