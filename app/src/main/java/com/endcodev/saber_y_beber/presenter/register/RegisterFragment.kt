package com.endcodev.saber_y_beber.presenter.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    companion object {
        const val TAG = "RegisterFragment **"
    }

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
        initListeners()
    }

    private fun initViews() {
        binding.viewHeader.headerTitle.text = resources.getString(R.string.register_account_title)
        binding.viewSignIn.btnLogin.text = resources.getString(R.string.register_bt)
    }

    private fun initObservers() {
        registerViewModel.pass.observe(viewLifecycleOwner) {
            binding.registerPassEt.error = it
        }

        registerViewModel.repeat.observe(viewLifecycleOwner) {
            binding.repeatPassEt.error = it
        }

        registerViewModel.email.observe(viewLifecycleOwner) {
            binding.registerMailEt.error = it
        }

        registerViewModel.user.observe(viewLifecycleOwner) {
            binding.registerUserEt.error = it
        }

        registerViewModel.dialog.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun initListeners() {
        binding.viewSignIn.btnLogin.setOnClickListener {
            getData()
        }

        //Back Button
        binding.viewHeader.headerBack.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun getData() {
        val email: String = binding.registerMailEt.text.toString()
        val pass: String = binding.registerPassEt.text.toString()
        val repeat: String = binding.repeatPassEt.text.toString()
        val userName: String = binding.registerUserEt.text.toString()
        try {
            registerViewModel.createAccount(email, pass, repeat, userName)
        } catch (e: Exception) {
            Log.e(TAG, "$e")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}