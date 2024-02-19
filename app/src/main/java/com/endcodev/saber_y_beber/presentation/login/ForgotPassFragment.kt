package com.endcodev.saber_y_beber.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.FragmentForgotPassBinding
import com.endcodev.saber_y_beber.presentation.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPassFragment : Fragment(R.layout.fragment_forgot_pass) {

    private var _binding: FragmentForgotPassBinding? = null
    private val binding: FragmentForgotPassBinding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentForgotPassBinding.inflate(
            inflater, container,
            false
        ).apply { _binding = this }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()
    }

    private fun initObservers() {

        viewModel.sent.observe(viewLifecycleOwner) {
            if (it) {
                context?.toast(resources.getString(R.string.forgot_mail_sent))
                findNavController().navigate(R.id.action_forgotPassFragment_to_loginFragment)
            } else
                context?.toast(resources.getString(R.string.forgot_error_send))
        }
    }

    private fun initListeners() {

        binding.viewHeader.headerBack.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        binding.forgotAcceptBt.setOnClickListener {
            viewModel.forgotPass(binding.forgotMail.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
