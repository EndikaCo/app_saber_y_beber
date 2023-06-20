package com.endcodev.saber_y_beber.presenter.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.data.model.PlayersModel
import com.endcodev.saber_y_beber.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    companion object {
        const val TAG = "HomeFragment **"
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var adapter: PlayerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()
        onBackPressed()
    }

    private fun initObservers() {
        homeViewModel.playerList.observe(viewLifecycleOwner) {
            initAdapter(it)
        }

        homeViewModel.isConnected.observe(viewLifecycleOwner){
            if (it) {
                binding.homeLoginBt.setBackgroundResource(R.drawable.user_login_button)
                binding.homeLoginBt.text = Firebase.auth.currentUser!!.displayName?.first().toString()
                binding.homeCreateBt.alpha = 1f
            } else {
                binding.homeLoginBt.setBackgroundResource(R.drawable.ic_login)
                binding.homeCreateBt.alpha = 0.3f
                binding.homeLoginBt.text = ""
            }
        }
    }

    private fun initListeners() {

        binding.homeCreateBt.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_correctFragment)
        }

        //Login imageButton click
        binding.homeLoginBt.setOnClickListener {
            toLoginFragment()
        }

        //button add player Onclick
        binding.homeAddBt.setOnClickListener {
            addPlayerDialog()
        }


    }

    /**Initialize recycler view [PlayerAdapter] */
    private fun initAdapter(list: MutableList<PlayersModel>) {
        adapter = PlayerAdapter(list, onDeleteClickListener = {
            homeViewModel.deletePlayer(list[it].name)
            homeViewModel.playerList.value!!.removeAt(it)
            adapter.notifyItemRemoved(it)
        })
        binding.homeRv.layoutManager = LinearLayoutManager(this.activity)
        binding.homeRv.adapter = adapter
    }

    /** show dialog [PlayerDialogFragment] to create new player*/
    private fun addPlayerDialog() {
        PlayerDialogFragment(
            onSubmitClickListener = { player ->
                homeViewModel.addNewPlayer(player)
                adapter.notifyItemInserted(homeViewModel.playerList.value!!.size)
            }
        ).show(parentFragmentManager, "dialog")
    }

    /** change fragment to LoginFragment*/
    private fun toLoginFragment() {
        if (Firebase.auth.currentUser != null && Firebase.auth.currentUser!!.isEmailVerified)
         //loginAlertDialog()
        else
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    }

    /**On BACK button pressed */
    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                    exitProcess(0)
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}