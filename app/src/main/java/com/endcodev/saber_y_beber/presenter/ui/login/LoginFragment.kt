package com.endcodev.saber_y_beber.presenter.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.FragmentLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val previewRequest =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                try {
                    val account = task.getResult(ApiException::class.java)
                    if (account != null) {
                        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                        val auth = FirebaseAuth.getInstance()
                        auth.signInWithCredential(credential)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    //todo falta meter el nombre si es la primera  vez y no tiene nombre de usuario,
                                    // como en register con un dialogo tal vez o coger el nombre que tiene en el mail
                                    Toast.makeText(
                                        requireContext(),
                                        "Sesión iniciada como ${auth.currentUser}",//todo aqui poner el name una vez exista
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                                } else
                                    Toast.makeText(
                                        requireContext(),
                                        "fail google",//todo a strings
                                        Toast.LENGTH_SHORT
                                    ).show()
                            }
                    }
                } catch (e: ApiException) {
                    Toast.makeText(requireContext(), "error:$e", Toast.LENGTH_SHORT).show()
                }
            }
        }


    private val callbackManager = CallbackManager.Factory.create()
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        //Mail + pass login button
        binding.viewSignIn.btnLogin.setOnClickListener {
            mailPassLogin()
        }

        //Facebook login Button
        binding.viewSignIn.registerFacebook.setOnClickListener {
            faceBookLogin()
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
        binding.loginBack.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    private fun mailPassLogin() {

        val auth: FirebaseAuth = Firebase.auth

        if (binding.loginMailEt.text.isNotEmpty() && binding.loginPassEt.text.isNotEmpty())
            auth.signInWithEmailAndPassword(
                binding.loginMailEt.text.toString(),
                binding.loginPassEt.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (!auth.currentUser?.isEmailVerified!!)
                        Toast.makeText(
                            context,
                            resources.getString(R.string.login_not_verified),
                            Toast.LENGTH_SHORT
                        ).show()
                    if (auth.currentUser?.isEmailVerified!!)
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.login_failed),
                        Toast.LENGTH_SHORT
                    ).show()
            }
        else
            Toast.makeText(context, resources.getString(R.string.login_error), Toast.LENGTH_SHORT)
                .show()
    }

    private fun googleLogin() {
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(requireContext(), googleConf)
        val signInIntent = Intent(googleClient.signInIntent)
        previewRequest.launch(signInIntent)
    }

    private fun faceBookLogin() {

        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    result?.let { login ->
                        val token = login.accessToken

                        val credential = FacebookAuthProvider.getCredential(token.token)

                        FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(requireContext(), "ok s", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Toast.makeText(requireContext(), "mal face", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    }
                }

                override fun onCancel() {
                    TODO("Not yet implemented")
                }

                override fun onError(error: FacebookException?) {
                    TODO("Not yet implemented")
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
