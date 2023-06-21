package com.endcodev.saber_y_beber.presenter.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.FragmentLoginBinding
import com.endcodev.saber_y_beber.databinding.FragmentProfileBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initViews()

    }

    private fun initViews() {
        binding.viewHeader.headerTitle.text = resources.getString(R.string.profile_title)
    }

    private fun initListeners() {
        //Back Button
        binding.viewHeader.headerBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }
}