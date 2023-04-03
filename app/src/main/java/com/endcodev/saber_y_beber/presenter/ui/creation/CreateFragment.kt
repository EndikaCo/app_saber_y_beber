package com.endcodev.saber_y_beber.presenter.ui.creation

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.FragmentCreationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateFragment : Fragment(R.layout.fragment_creation) {

    private var _binding: FragmentCreationBinding? = null
    private val binding: FragmentCreationBinding get() = _binding!!
    private val createViewModel: CreateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCreationBinding.inflate(
            inflater, container,
            false
        ).apply { _binding = this }.root
    }

}