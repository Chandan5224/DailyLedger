package com.dev.dailyledger.ui.auth.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dev.dailyledger.MainActivity
import com.dev.dailyledger.R
import com.dev.dailyledger.databinding.FragmentVerificationBinding
import com.dev.dailyledger.ui.auth.AuthActivity
import com.dev.dailyledger.ui.auth.AuthViewModel
import com.dev.dailyledger.utils.Constants
import com.dev.dailyledger.utils.Constants.ONBOARDING_AUTH
import com.dev.dailyledger.utils.Resource


class VerificationFragment : Fragment() {

    private lateinit var binding: FragmentVerificationBinding
    private lateinit var email: String
    private var isCodeSendAllowed = false  // track if user is allowed to send code again
    private val authViewModel: AuthViewModel by activityViewModels {
        (requireActivity() as AuthActivity).authViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerificationBinding.inflate(layoutInflater, container, false)

        email = arguments?.getString(Constants.SIGNUP_EMAIL)!!
        setVerificationMessage(email)
        setTimer()
        onClickHandle()
        observeData()
        return binding.root
    }

    private fun onClickHandle() {
        binding.txtSendCodeAgain.setOnClickListener {
            if (isCodeSendAllowed) {
                Toast.makeText(requireContext(), "Code Sent!", Toast.LENGTH_SHORT).show()
                setTimer()
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnVerify.setOnClickListener {
            val code = binding.firstPinView.text.toString().trim()
            authViewModel.verifyEmail(email,code)
        }
    }
    private fun observeData() {
        authViewModel.verifyEmailResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                when (it) {
                    is Resource.Error -> {
                        binding.loader.visibility = View.GONE
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Loading -> {
                        binding.loader.visibility = View.VISIBLE
                        binding.loader.playAnimation()
                    }

                    is Resource.Success -> {
                        Toast.makeText(binding.root.context, it.data, Toast.LENGTH_SHORT).show()
                        binding.loader.cancelAnimation()
                        binding.loader.visibility = View.INVISIBLE
                        authViewModel.saveDataInSharePreference(Constants.IS_LOGIN,"TRUE")
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finishAffinity()
                    }
                }
            }
        }

    }
    private fun setVerificationMessage(email: String) {

        val fullText = "We sent a verification code to your email $email. You can check your inbox."

        val spannable = android.text.SpannableString(fullText)

        // Find where the email starts and ends
        val start = fullText.indexOf(email)
        val end = start + email.length

        // Apply color span only to the email
        val colorSpan = android.text.style.ForegroundColorSpan(
            requireContext().getColor(R.color.color_ic_nav) // or your custom color
        )
        spannable.setSpan(colorSpan, start, end, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.txtVerificationMessage.text = spannable

    }

    private fun setTimer() {
        isCodeSendAllowed = false
        binding.txtSendCodeAgain.isClickable = false
        binding.txtSendCodeAgain.alpha = 0.5f

        val timer = object : CountDownTimer(5 * 60 * 1000, 1000) {  // 5 minutes
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.txtTimer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.txtTimer.text = "00:00"
                isCodeSendAllowed = true
                binding.txtSendCodeAgain.isClickable = true
                binding.txtSendCodeAgain.alpha = 1f
            }
        }

        timer.start()
    }
}
