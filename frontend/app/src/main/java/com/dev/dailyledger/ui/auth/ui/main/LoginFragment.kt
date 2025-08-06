package com.dev.dailyledger.ui.auth.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dev.dailyledger.MainActivity
import com.dev.dailyledger.R
import com.dev.dailyledger.databinding.FragmentLoginBinding
import com.dev.dailyledger.ui.auth.AuthActivity
import com.dev.dailyledger.ui.auth.AuthViewModel
import com.dev.dailyledger.utils.Constants
import com.dev.dailyledger.utils.Resource

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val authViewModel: AuthViewModel by activityViewModels {
        (requireActivity() as AuthActivity).authViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        setSignupSpannableText()
        onClickHandles()
        observeData()

        binding.etEmail.addTextChangedListener {
            binding.emailLayout.error = null
        }

        binding.etPassword.addTextChangedListener {
            binding.passwordLayout.error = null
        }

        return binding.root
    }

    private fun onClickHandles() {
        binding.btnBack.setOnClickListener {
            if (!findNavController().popBackStack()) {
                binding.btnBack.setOnClickListener {
                    requireActivity().finish()
                }
            }
        }

        binding.txtForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPwdFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (checkValidation(email, password)) {
                return@setOnClickListener
            }
            authViewModel.login(email, password)
        }
    }

    private fun observeData() {
        authViewModel.loginResponse.observe(viewLifecycleOwner) { response ->
            response?.let { it ->
                when (it) {
                    is Resource.Error -> {
                        binding.loader.cancelAnimation()
                        binding.loader.visibility = View.INVISIBLE
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Loading -> {
                        binding.loader.visibility = View.VISIBLE
                        binding.loader.playAnimation()
                    }

                    is Resource.Success -> {
                        it.data?.let {
                            when (it.header.code) {
                                200 -> {
                                    Toast.makeText(
                                        binding.root.context,
                                        it.body.value.asString,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.loader.cancelAnimation()
                                    binding.loader.visibility = View.INVISIBLE
                                    authViewModel.saveDataInSharePreference(
                                        Constants.IS_LOGIN, "TRUE"
                                    )
                                    val intent = Intent(requireActivity(), MainActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finishAffinity()
                                }

                                201 -> {
                                    Toast.makeText(
                                        binding.root.context, it.body.error, Toast.LENGTH_SHORT
                                    ).show()
                                    val email = binding.etEmail.text.toString().trim()
                                    authViewModel.resendOTP(email)
                                    val bundle = Bundle()
                                    bundle.putString(
                                        Constants.SIGNUP_EMAIL, email
                                    )
                                    findNavController().navigate(R.id.action_loginFragment_to_verificationFragment,bundle)
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private fun checkValidation(email: CharSequence?, password: CharSequence?): Boolean {
        var errorCheck = false
        if (email.isNullOrEmpty()) {
            binding.emailLayout.error = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.error = "Enter a valid email"
            errorCheck = true
        }
        if (password.isNullOrEmpty()) {
            binding.passwordLayout.error = "Password is required"
            errorCheck = true
        }
        return errorCheck
    }


    private fun setSignupSpannableText() {
        val fullText = getString(R.string.don_t_have_an_account_yet_sign_up)
        val loginText = "Sign Up"

        val spannableString = SpannableString(fullText)
        val startIndex = fullText.indexOf(loginText)
        val endIndex = startIndex + loginText.length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Replace with your actual action ID
                findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
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

        binding.txtNotHaveSignUp.text = spannableString
        binding.txtNotHaveSignUp.movementMethod = LinkMovementMethod.getInstance()
        binding.txtNotHaveSignUp.highlightColor = Color.TRANSPARENT
    }

    override fun onPause() {
        super.onPause()
        authViewModel.loginResponse.postValue(null)
    }

}