package com.endcodev.saber_y_beber.presenter.correction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.FragmentCorrectBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess


@AndroidEntryPoint
class CorrectFragment : Fragment(R.layout.fragment_correct) {

    private var _binding: FragmentCorrectBinding? = null
    private val binding: FragmentCorrectBinding get() = _binding!!
    private val correctViewModel: CorrectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCorrectBinding.inflate(
            inflater, container,
            false
        ).apply { _binding = this }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        onBackPressed()
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.homeFragment)
                }
            }
        )
    }

    private fun initListeners() {
        binding.viewHeader.headerTitle.text = resources.getString(R.string.correction_title_default)
        binding.viewHeader.headerBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }
}