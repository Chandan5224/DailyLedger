package com.dev.dailyledger.ui.auth.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.dailyledger.R
import com.dev.dailyledger.databinding.FragmentResetPwdBinding

class ResetPwdFragment : Fragment() {

    private lateinit var binding: FragmentResetPwdBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentResetPwdBinding.inflate(layoutInflater,container,false)
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}