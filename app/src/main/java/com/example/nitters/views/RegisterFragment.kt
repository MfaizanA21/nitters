package com.example.nitters.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nitters.R
import com.example.nitters.databinding.FragmentRegisterBinding
import com.example.nitters.di.utils.NetworkResult
import com.example.nitters.fireViewModel.FireAuthViewModel
import com.example.nitters.models.UserRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val fireAuthViewModel by viewModels<FireAuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {

            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()
            val username = binding.txtUsername.text.toString()
            val validateInput = validate()
            if(validateInput.first) {
                fireAuthViewModel.signup(username, email, password)
            } else {
                binding.txtError.text = validateInput.second
            }


            fireAuthViewModel.signupFlow.observe(viewLifecycleOwner) {
                when(it) {
                    is NetworkResult.Success -> {
                        findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                    }
                    is NetworkResult.Error -> {
                        binding.txtError.text = it.message
                    }
                    is NetworkResult.Loading -> {
                        TODO()
                        //Loading Bar
                    }

                    else -> {}
                }
            }

        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val username = binding.txtUsername.text.toString()
        val password = binding.txtPassword.text.toString()

        return UserRequest(emailAddress, username, password)
    }

    private fun validate(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return fireAuthViewModel.validation(userRequest.username, userRequest.email, userRequest.password, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}