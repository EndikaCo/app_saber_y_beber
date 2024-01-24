package com.endcodev.saber_y_beber.presentation.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.data.network.AuthenticationService.Companion.ERROR_CREATING_ACC
import com.endcodev.saber_y_beber.data.network.AuthenticationService.Companion.MAIL_SENT_ERROR
import com.endcodev.saber_y_beber.data.network.AuthenticationService.Companion.MAIL_SENT_SUCCESS
import com.endcodev.saber_y_beber.databinding.FragmentRegisterBinding
import com.endcodev.saber_y_beber.domain.model.ErrorModel
import com.endcodev.saber_y_beber.domain.utils.App
import com.endcodev.saber_y_beber.presentation.dialogs.ErrorDialogFragment
import com.endcodev.saber_y_beber.presentation.login.RegisterViewModel.Companion.PASS_CAP
import com.endcodev.saber_y_beber.presentation.login.RegisterViewModel.Companion.PASS_DIGIT
import com.endcodev.saber_y_beber.presentation.login.RegisterViewModel.Companion.PASS_MINUS
import com.endcodev.saber_y_beber.presentation.login.RegisterViewModel.Companion.PASS_SHORT
import com.endcodev.saber_y_beber.presentation.login.RegisterViewModel.Companion.PASS_SPECIAL
import com.endcodev.saber_y_beber.presentation.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

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
        binding.viewSignIn.btnLogin.text = resources.getString(R.string.register_bt)
        binding.viewSignIn.registerGoogle.visibility = View.GONE
        binding.viewSignIn.tvLogin.visibility = View.GONE
    }

    private fun initObservers() {
        viewModel.pass.observe(viewLifecycleOwner) {

            var error = ""

            when (it) {
                PASS_SHORT -> error = resources.getString(R.string.register_error_pass)
                PASS_DIGIT -> error = resources.getString(R.string.register_error_pass2)
                PASS_CAP -> error = resources.getString(R.string.register_error_pass_uppercase)
                PASS_MINUS -> error = resources.getString(R.string.register_error_pass_lowercase)
                PASS_SPECIAL -> error = resources.getString(R.string.register_error_pass_special)
            }
            binding.registerPassEt.error = error
        }

        viewModel.repeat.observe(viewLifecycleOwner) {
            binding.repeatPassEt.error = it
        }

        viewModel.email.observe(viewLifecycleOwner) {
            binding.registerMailEt.error = it
        }

        viewModel.user.observe(viewLifecycleOwner) {
            binding.registerUserEt.error = it
        }

        viewModel.dialog.observe(viewLifecycleOwner) {

            when (it) {
                ERROR_CREATING_ACC -> context?.toast(resources.getString(R.string.register_error_creating_acc))
                MAIL_SENT_ERROR -> context?.toast(resources.getString(R.string.error_sending_mail))
                MAIL_SENT_SUCCESS -> {
                    dialogError(
                        ErrorModel(
                            resources.getString(R.string.register_success),
                            resources.getString(R.string.register_check_mail),
                            resources.getString(R.string.player_accept),
                            ""
                        )
                    )
                }
            }
        }
    }

    private fun dialogError(errorModel: ErrorModel) {

        val dialog = ErrorDialogFragment(
            onAcceptClickLister = {
                if (it) {
                    findNavController().navigate(R.id.loginFragment)
                } else {
                    findNavController().navigate(R.id.loginFragment)
                }
            }, ErrorModel(
                errorModel.title,
                errorModel.description,
                errorModel.acceptButton,
                errorModel.cancelButton
            )
        )
        dialog.isCancelable = false
        dialog.show(parentFragmentManager, "dialog")
    }

    private fun initListeners() {
        //Sign Up Button
        binding.viewSignIn.btnLogin.setOnClickListener {
            getData()
        }

        //Back Button
        binding.viewHeader.headerBack.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        //Back Button
        binding.loginCreateLink2.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun getData() {
        val email: String = binding.registerMailEt.text.toString()
        val pass: String = binding.registerPassEt.text.toString()
        val repeat: String = binding.repeatPassEt.text.toString()
        val userName: String = binding.registerUserEt.text.toString()
        try {
            viewModel.createAccount(email, pass, repeat, userName)
        } catch (e: Exception) {
            Log.e(App.tag, "Error: $e")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}