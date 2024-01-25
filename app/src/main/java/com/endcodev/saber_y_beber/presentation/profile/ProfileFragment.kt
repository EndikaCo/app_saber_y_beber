package com.endcodev.saber_y_beber.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.FragmentProfileBinding
import com.endcodev.saber_y_beber.domain.model.ErrorModel
import com.endcodev.saber_y_beber.domain.model.ProfileModel
import com.endcodev.saber_y_beber.presentation.dialogs.ErrorDialogFragment
import com.endcodev.saber_y_beber.presentation.dialogs.NameDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var adapter: ProfileAdapter


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
        initObservers()
        initViews()
    }

    private fun initObservers() {

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.profileProgress.isVisible = it
        }

        viewModel.currentUser.observe(viewLifecycleOwner) {
            binding.profileUsername.text = it.displayName
            binding.profileMail.text = it.email
        }

        viewModel.activityList.observe(viewLifecycleOwner) {
            initAdapter(it)
        }
    }

    private fun initViews() {
        viewModel.user()
    }

    private fun initListeners() {
        //Back Button
        binding.profileBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        binding.profileLogout.setOnClickListener {
            viewModel.disconnect()
            findNavController().navigate(R.id.homeFragment)
        }

        binding.profileChangeUsername?.setOnClickListener { //todo
            NameDialogFragment(
                onAccept = {
                    viewModel.changeUserName(it)
                    findNavController().navigate(R.id.homeFragment)
                }
            ).show(parentFragmentManager, "dialog")
        }

        binding.profileDeleteAcc?.setOnClickListener {

            ErrorDialogFragment(
                onAcceptClickLister = {
                    if (it)
                        deleteAccount()
                },
                ErrorModel(
                    title = "Eliminar cuenta",
                    description = "¿Estás seguro de que quieres eliminar tu cuenta?",
                    acceptButton = "Sí",
                    cancelButton = "No"
                )
            ).show(parentFragmentManager, "dialog")
        }
    }

    fun deleteAccount() {
        viewModel.deleteAccount(onComplete = {
            if (it) {
                findNavController().navigate(R.id.homeFragment)
                Toast.makeText(
                    requireContext(),
                    "Cuenta eliminada correctamente", //todo
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                ErrorDialogFragment(
                    onAcceptClickLister = {
                    },
                    ErrorModel(
                        title = "Error",
                        description = "No se ha podido eliminar la cuenta",
                        acceptButton = "OK",
                        ""
                    )
                ).show(parentFragmentManager, "dialog")
            }
        })
    }

    //Recycler adapter
    private fun initAdapter(list: List<ProfileModel>?) {
        list?.let {
            adapter = ProfileAdapter(it)
            binding.profileRv.layoutManager = LinearLayoutManager(this.activity)
            binding.profileRv.adapter = adapter
        }
    }
}