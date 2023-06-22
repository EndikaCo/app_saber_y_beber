package com.endcodev.saber_y_beber.presenter.forgot_pass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.FragmentForgotPassBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassFragment : Fragment(R.layout.fragment_forgot_pass) {

    private var _binding: FragmentForgotPassBinding? = null
    private val binding: FragmentForgotPassBinding get() = _binding!!
    private lateinit var auth: FirebaseAuth

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

        initViews()
        initListeners()
    }

    private fun initViews() {
        binding.viewHeader.headerTitle.text = resources.getString(R.string.forgot_title)
    }

    private fun initListeners() {

        binding.viewHeader.headerBack.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        binding.forgotAcceptBt.setOnClickListener {
            val email = binding.forgotMail.text
            auth = FirebaseAuth.getInstance()
            auth.sendPasswordResetEmail(email.toString())
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.forgot_mail_sent),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_forgotPassFragment_to_loginFragment)
                }.addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.forgot_error_send),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }


}
