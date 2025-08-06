package com.dev.dailyledger.ui.auth.ui.main

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.dailyledger.R
import com.dev.dailyledger.databinding.FragmentForgotPwdBinding

class ForgotPwdFragment : Fragment() {

    private lateinit var binding: FragmentForgotPwdBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentForgotPwdBinding.inflate(layoutInflater,container,false)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnContinue.setOnClickListener {
            val email= binding.etEmail.text.toString().trim()

            if (email.isEmpty()) {
                binding.etEmail.error = "Email is required"
                return@setOnClickListener
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmail.error = "Enter a valid email"
                return@setOnClickListener
            }

            findNavController().navigate(R.id.action_forgotPwdFragment_to_resetPwdFragment)

        }

        return binding.root
    }
}