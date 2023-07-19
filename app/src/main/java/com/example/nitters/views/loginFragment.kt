package com.example.nitters.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nitters.R
import com.example.nitters.databinding.FragmentLoginBinding
import com.example.nitters.di.utils.NetworkResult
import com.example.nitters.fireViewModel.FireAuthViewModel
import com.example.nitters.models.UserRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val fireAuthViewModel by viewModels<FireAuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {

            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()

            val validateInput = validate()
            if(validateInput.first) {
                fireAuthViewModel.login(email, password)
            } else {
                binding.txtError.text = validateInput.second
            }

            fireAuthViewModel.loginFlow.observe(viewLifecycleOwner) {
                when(it) {
                    is NetworkResult.Success -> {
                        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    }
                    is NetworkResult.Error -> {
                        binding.txtError.text = it.message
                    }
                    is NetworkResult.Loading -> {
                        TODO()
                        //progress bar
                    }

                    else -> {}
                }
            }

        }

        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()

        return UserRequest(emailAddress,password,"" )

    }

    private fun validate(): Pair<Boolean, String> {
        val userRequest = getUserRequest()

        return fireAuthViewModel.validation(userRequest.username, userRequest.email, userRequest.password, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}