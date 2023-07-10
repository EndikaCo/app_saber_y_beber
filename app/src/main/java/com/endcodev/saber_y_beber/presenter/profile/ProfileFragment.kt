package com.endcodev.saber_y_beber.presenter.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.data.model.ProfileModel
import com.endcodev.saber_y_beber.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        initAdapter(viewModel.activityList.value!!)
    }

    private fun initObservers() {
        viewModel.currentUser.observe(viewLifecycleOwner){
            binding.profileUsername.text = it.displayName
            binding.profileMail.text = it.email
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