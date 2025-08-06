package com.dev.dailyledger.ui.auth.ui.main

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dev.dailyledger.R
import com.dev.dailyledger.databinding.FragmentSignupBinding
import com.dev.dailyledger.model.User
import com.dev.dailyledger.ui.auth.AuthActivity
import com.dev.dailyledger.ui.auth.AuthViewModel
import com.dev.dailyledger.utils.Constants
import com.dev.dailyledger.utils.Resource

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private val authViewModel: AuthViewModel by activityViewModels {
        (requireActivity() as AuthActivity).authViewModelFactory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(layoutInflater, container, false)
        setLoginSpannableText()
        onClickHandles()
        observeData()
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        authViewModel.signupResponse.postValue(null)
    }

    private fun observeData() {
        authViewModel.signupResponse.observe(viewLifecycleOwner) { response ->
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
                        val bundle = Bundle()
                        bundle.putString(
                            Constants.SIGNUP_EMAIL,
                            binding.etEmail.text.toString().trim()
                        )
                        findNavController().navigate(
                            R.id.action_signupFragment_to_verificationFragment,
                            bundle
                        )
                    }
                }
            }
        }

    }

    private fun onClickHandles() {
        binding.etPassword.addTextChangedListener {
            val passwordPattern =
                Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!]).{6,}\$")
            if (!passwordPattern.matches(binding.etPassword.text.toString()) && !binding.etPassword.text.isNullOrEmpty()) {
                binding.passwordLayout.error =
                    "Weak password. Must be 6+ chars with upper, lower, digit, and symbol"
            } else {
                binding.passwordLayout.error = null
            }
        }

        binding.btnBack.setOnClickListener {
            if (!findNavController().popBackStack()) {
                binding.btnBack.setOnClickListener {
                    requireActivity().finish()
                }
            }
        }
        binding.btnSignup.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (checkValidation(name, email, password)) {
                return@setOnClickListener
            }
            authViewModel.signup(User(0, name, email, password))
        }
    }

    private fun checkValidation(
        name: CharSequence?, email: CharSequence?, password: CharSequence?
    ): Boolean {
        var errorCheck = false
        if (name.isNullOrEmpty()) {
            binding.etName.error = "Name is required"
            errorCheck = true
        }
        if (email.isNullOrEmpty()) {
            binding.etEmail.error = "Email is required"
            errorCheck = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Enter a valid email"
            errorCheck = true
        }
        if (password.isNullOrEmpty()) {
            binding.etPassword.error = "Password is required"
            errorCheck = true
        }

        if (!binding.chkTerms.isChecked) {
            // Show temp checked
            binding.chkTerms.isChecked = true

            // Save original color
            val originalTint = binding.chkTerms.buttonTintList

            // Set tint to red
            binding.chkTerms.buttonTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    binding.root.context, android.R.color.holo_red_dark
                )
            )

            val shake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
            binding.chkTerms.startAnimation(shake)

            // Wait 1 second then uncheck and remove red background
            binding.chkTerms.postDelayed({
                binding.chkTerms.isChecked = false
                binding.chkTerms.background = null
                binding.chkTerms.buttonTintList = originalTint
            }, 1000)

            errorCheck = true
        }

        return errorCheck
    }

    private fun setLoginSpannableText() {
        val fullText = getString(R.string.already_have_an_account_login)
        val loginText = "Login"

        val spannableString = SpannableString(fullText)
        val startIndex = fullText.indexOf(loginText)
        val endIndex = startIndex + loginText.length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Replace with your actual action ID
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.color_ic_nav)
                ds.isUnderlineText = false
                ds.isFakeBoldText = true
            }
        }

        spannableString.setSpan(
            clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.txtLogin.text = spannableString
        binding.txtLogin.movementMethod = LinkMovementMethod.getInstance()
        binding.txtLogin.highlightColor = Color.TRANSPARENT
    }

}