package com.league.leaguestats.ui.tft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.league.leaguestats.R

class TFTFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tft, container, false)

        val tftViewModel = ViewModelProvider(this)[TFTViewModel::class.java]

        val textView: TextView = root.findViewById(R.id.text_tft)
        tftViewModel.titleText.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
}
