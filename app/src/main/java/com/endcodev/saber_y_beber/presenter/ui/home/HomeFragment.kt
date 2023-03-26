package com.endcodev.saber_y_beber.presenter.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        checkLogin()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Login imageButton click
        binding.homeLoginBt.setOnClickListener {
            toLoginFragment()
        }
    }




    /** change fragment to LoginFragment*/
    private fun toLoginFragment() {
        if (Firebase.auth.currentUser != null && Firebase.auth.currentUser!!.isEmailVerified)
            loginAlertDialog()
        else
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    }

    /** show AlertDialog when already logged*/
    private fun loginAlertDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setTitle("You are currently logged as ${Firebase.auth.currentUser?.displayName.toString()}")
        alertDialog.setMessage("Do you want to logout?")
        alertDialog.setIcon(getDrawable(requireContext(), R.drawable.login_bt))
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "Continue logged in"
        ) { dialog, _ -> dialog.dismiss() }

        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, "Logout"
        ) { dialog, _ ->
            dialog.dismiss()
            Firebase.auth.signOut()
            checkLogin()
        }
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.show()
        val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        btnNegative.setTextColor(Color.RED)
        val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 10f
        btnPositive.layoutParams = layoutParams
        btnNegative.layoutParams = layoutParams
    }

    /** checks if user is logged*/
    private fun checkLogin() {
        if (Firebase.auth.currentUser != null && Firebase.auth.currentUser!!.isEmailVerified) {
            binding.homeLoginBt.setBackgroundResource(R.drawable.user_login_button)
            binding.homeLoginBt.text = Firebase.auth.currentUser!!.displayName?.first().toString()
            //binding.homeCreateBt.alpha = 1f
        } else {
            binding.homeLoginBt.setBackgroundResource(R.drawable.login_bt)
            //binding.homeCreateBt.alpha = 0.3f
            binding.homeLoginBt.text = ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}