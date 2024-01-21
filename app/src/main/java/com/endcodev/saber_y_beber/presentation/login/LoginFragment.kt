package com.endcodev.saber_y_beber.presentation.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    private val gLogin =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            gLoginInit(result)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
        initListeners()
    }

    private fun initViews() {
        binding.viewHeader.headerTitle.text = resources.getString(R.string.login_title)
    }

    private fun initObservers() {
        loginViewModel.toast.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        loginViewModel.isConnected.observe(viewLifecycleOwner) {
            if (it)
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    private fun initListeners() {
        //Mail + pass login button
        binding.viewSignIn.btnLogin.setOnClickListener {
            mailPassLogin()
        }

        //Google login button
        binding.viewSignIn.registerGoogle.setOnClickListener {
            googleLogin()
        }

        //Create Account Link
        binding.loginCreateLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        //forgot pass Link
        binding.loginForgotLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPassFragment)
        }

        //Back Button
        binding.viewHeader.headerBack.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    private fun googleLogin() {
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(requireContext(), googleConf)
        val signInIntent = Intent(googleClient.signInIntent)
        gLogin.launch(signInIntent)
    }

    private fun gLoginInit(result: ActivityResult?) {

        if (result != null) {

            if (result.resultCode == Activity.RESULT_OK) {

                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                try {
                    val account = task.getResult(ApiException::class.java)
                    if (account != null)
                        gLogin(account)
                } catch (e: ApiException) {
                    Toast.makeText(requireContext(), "error:$e", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun gLogin(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        val auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.login_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.something_went_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
            }

    }

    private fun mailPassLogin() {
        loginViewModel.login(
            binding.loginMailEt.text.toString(),
            binding.loginPassEt.text.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
