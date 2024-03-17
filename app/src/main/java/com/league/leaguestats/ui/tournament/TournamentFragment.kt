package com.league.leaguestats.ui.tournament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.league.leaguestats.R

class TournamentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tournament, container, false)

        val tournamentViewModel = ViewModelProvider(this)[TournamentViewModel::class.java]

        val textView: TextView = root.findViewById(R.id.text_tournament)
        tournamentViewModel.titleText.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
}
