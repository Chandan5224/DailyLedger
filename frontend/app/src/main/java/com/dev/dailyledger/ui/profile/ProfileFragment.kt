package com.dev.dailyledger.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.dailyledger.R
import com.dev.dailyledger.databinding.FragmentProfileBinding
import com.dev.dailyledger.ui.onboarding.OnboardingActivity
import com.dev.dailyledger.utils.AppPreferences
import com.dev.dailyledger.utils.Constants

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        binding.btnLogout.setOnClickListener {
            AppPreferences.removeDataSharePreference(Constants.IS_LOGIN)
            val intent = Intent(requireActivity(), OnboardingActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity()
        }
        return binding.root
    }

}