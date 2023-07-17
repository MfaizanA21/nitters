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
import com.example.nitters.di.utils.TokenManager
import com.example.nitters.models.UserRequest
import com.example.nitters.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

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

            val validationResult = validateUserInput()
            if(validationResult.first) {
                authViewModel.loginUser(getUserRequest())
            }
            else {
                binding.txtError.text = validationResult.second
            }

            bindObserver()
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }

                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }

                is NetworkResult.Loading -> {
                    //Progress Bar should be shown
                }
            }
        }

    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()

        return UserRequest(emailAddress,password,"" )

    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()

        return authViewModel.validateCredential(userRequest.username, userRequest.email, userRequest.password, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}