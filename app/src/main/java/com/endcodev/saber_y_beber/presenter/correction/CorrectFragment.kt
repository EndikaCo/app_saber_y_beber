package com.endcodev.saber_y_beber.presenter.correction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.FragmentCorrectBinding
import dagger.hilt.android.AndroidEntryPoint


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

}