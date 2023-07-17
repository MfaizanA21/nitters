package com.example.nitters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.nitters.databinding.FragmentRegisterBinding
import com.example.nitters.di.utils.NetworkResult
import com.example.nitters.di.utils.TokenManager
import com.example.nitters.models.UserRequest
import com.example.nitters.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager //when signup/login call success we get token

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)


        if(tokenManager.getToken() !=null) {
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {

            val validationResult = validateUserInput()

            if(validationResult.first){


                authViewModel.registerUser(getUserRequest())
            }
            else {
                binding.txtError.text = validationResult.second
            }

        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        bindObserver()

    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val username = binding.txtUsername.text.toString()
        val password = binding.txtPassword.text.toString()

        return UserRequest(emailAddress, username, password)
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredential(userRequest.username, userRequest.email, userRequest.password, false)

    }

    private fun bindObserver() {

        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer{
            when(it){
                is  NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)

                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                is  NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
                is NetworkResult.Loading -> {
                    //binding.progressBar.isVisible = true
                }
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}