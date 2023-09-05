package com.endcodev.saber_y_beber.presenter.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.data.model.ErrorModel
import com.endcodev.saber_y_beber.data.network.AuthenticationService.Companion.ERROR_CREATING_ACC
import com.endcodev.saber_y_beber.data.network.AuthenticationService.Companion.MAIL_SENT_ERROR
import com.endcodev.saber_y_beber.data.network.AuthenticationService.Companion.MAIL_SENT_SUCCESS
import com.endcodev.saber_y_beber.databinding.FragmentRegisterBinding
import com.endcodev.saber_y_beber.presenter.dialogs.ErrorDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    companion object {
        const val TAG = "RegisterFragment ***"
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
        binding.viewSignIn.registerGoogle.visibility = View.GONE
        binding.viewSignIn.registerFacebook.visibility = View.GONE
        binding.viewSignIn.tvLogin.visibility = View.GONE
    }

    private fun initObservers() {
        registerViewModel.pass.observe(viewLifecycleOwner) {

            var error = ""

            when (it) {
                RegisterViewModel.PASS_SHORT -> error =
                    resources.getString(R.string.register_error_pass)

                RegisterViewModel.PASS_DIGIT -> error =
                    resources.getString(R.string.register_error_pass2)

                RegisterViewModel.PASS_CAP -> error =
                    resources.getString(R.string.register_error_pass_uppercase)

                RegisterViewModel.PASS_MINUS -> error =
                    resources.getString(R.string.register_error_pass_lowercase)

                RegisterViewModel.PASS_SPECIAL -> error =
                    resources.getString(R.string.register_error_pass_special)
            }
            binding.registerPassEt.error = error
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

            if (it == ERROR_CREATING_ACC)
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.register_error_creating_acc),
                    Toast.LENGTH_LONG
                ).show()
            else if (it == MAIL_SENT_ERROR)
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.error_sending_mail),
                    Toast.LENGTH_LONG
                ).show()
            else if (it == MAIL_SENT_SUCCESS)
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
            registerViewModel.createAccount(email, pass, repeat, userName)
        } catch (e: Exception) {
            Log.e(TAG, "Error: $e")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}