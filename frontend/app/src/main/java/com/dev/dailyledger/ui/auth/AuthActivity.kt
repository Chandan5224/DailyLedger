package com.dev.dailyledger.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.dev.dailyledger.R
import com.dev.dailyledger.databinding.ActivityAuthBinding
import com.dev.dailyledger.utils.Constants

class AuthActivity : AppCompatActivity() {

    // Make this accessible from Fragments
    val authViewModelFactory by lazy {
        AuthViewModelFactory(AuthRepository(this))
    }

    val authViewModel: AuthViewModel by viewModels { authViewModelFactory }

    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mode = intent.getStringExtra(Constants.ONBOARDING_AUTH)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_auth) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.auth_navigation)

        if(mode.equals("SIGNUP"))
            navGraph.setStartDestination(R.id.signupFragment)
        else
            navGraph.setStartDestination(R.id.loginFragment)

        navController.graph=navGraph

    }
}