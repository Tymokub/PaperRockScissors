package com.paperrockscissors.view.authView

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.paperrockscissors.R
import com.paperrockscissors.databinding.AuthFragmentBinding

class AuthFragment : Fragment() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: AuthFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AuthFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding.authLoginNewBtn.setOnClickListener {
            viewModel.toggleAuthMode()
        }

        binding.authLoginBtn.setOnClickListener {
            val username = binding.authLoginUsername.text.toString()
            val password = binding.authLoginPassword.text.toString()
            viewModel.onActionBtnClick(username, password,
                {view.findNavController().navigate(R.id.action_authFragment_to_gameMenuFragment)},
                {Toast.makeText(view.context, "Niepoprawne dane",
                    Toast.LENGTH_SHORT).show()})
        }

        viewModel.currentAuthMode.observe(viewLifecycleOwner) {
            when (it) {
                AuthMode.SIGNIN -> {
                    binding.authWelcomeText.text = view.resources.getText(R.string.welcomeTextNew)
                    binding.authLoginBtn.text = view.resources.getText(R.string.registerBtn)
                    binding.authLoginNewBtn.text = view.resources.getText(R.string.oldAccount)
                }

                AuthMode.SIGNUP -> {
                    binding.authWelcomeText.text = view.resources.getText(R.string.welcomeText)
                    binding.authLoginBtn.text = view.resources.getText(R.string.loginBtn)
                    binding.authLoginNewBtn.text = view.resources.getText(R.string.newAccount)
                }
            }
        }
    }
}