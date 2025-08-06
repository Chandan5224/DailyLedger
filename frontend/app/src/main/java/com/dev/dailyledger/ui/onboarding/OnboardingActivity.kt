package com.dev.dailyledger.ui.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dev.dailyledger.MainActivity
import com.dev.dailyledger.R
import com.dev.dailyledger.adapter.ViewpagerAdapter
import com.dev.dailyledger.databinding.ActivityOnboardingBinding
import com.dev.dailyledger.ui.auth.AuthActivity
import com.dev.dailyledger.utils.AppPreferences
import com.dev.dailyledger.utils.Constants
import com.dev.dailyledger.utils.Constants.ONBOARDING_AUTH

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewpagerAdapter: ViewpagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (AppPreferences.getDataFromSharePreference(Constants.IS_LOGIN).equals("TRUE")) {
            val intent = Intent(this@OnboardingActivity, MainActivity::class.java)
            startActivity(intent)
            this@OnboardingActivity.finish()
        }

        setUpViewPager()

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@OnboardingActivity, AuthActivity::class.java)
            intent.putExtra(ONBOARDING_AUTH, "LOGIN")
            startActivity(intent)
        }

        binding.btnSignup.setOnClickListener {
            val intent = Intent(this@OnboardingActivity, AuthActivity::class.java)
            intent.putExtra(ONBOARDING_AUTH, "SIGNUP")
            startActivity(intent)
        }
    }


    private fun setUpViewPager() {
        //create list of fragments
        val listOfFragments = listOf(viewpager_page1(), viewpager_page2(), viewpager_page3())

        // initialize adapter
        viewpagerAdapter = ViewpagerAdapter(
            listOfFragments,
            supportFragmentManager,
            lifecycle
        )

        //set the adapter onto viewpager
        binding.viewPager.adapter = viewpagerAdapter

        binding.indicatorView.setupWithViewPager(binding.viewPager)
    }
}