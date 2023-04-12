package com.endcodev.saber_y_beber.presenter.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.data.model.PlayersModel
import com.endcodev.saber_y_beber.databinding.FragmentHomeBinding
import com.endcodev.saber_y_beber.presenter.adapter.PlayerAdapter
import com.endcodev.saber_y_beber.presenter.ui.dialogs.PlayerDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var adapter: PlayerAdapter

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

        initListeners()
    }

    private fun initListeners() {

        //button add player Onclick
        binding.homeAddBt.setOnClickListener {
            //add todo
        }

        //Login imageButton click
        binding.homeLoginBt.setOnClickListener {
            toLoginFragment()
        }

        //button add player Onclick
        binding.homeAddBt.setOnClickListener {
            addPlayerDialog()
        }

        homeViewModel.playerList.observe(viewLifecycleOwner) {
            initAdapter(it)
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
        // loginAlertDialog() todo
        else
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    }

    /** checks if user is logged*/
    private fun checkLogin() {
        if (Firebase.auth.currentUser != null && Firebase.auth.currentUser!!.isEmailVerified) {
            binding.homeLoginBt.setBackgroundResource(R.drawable.user_login_button)
            binding.homeLoginBt.text = Firebase.auth.currentUser!!.displayName?.first().toString()
            //binding.homeCreateBt.alpha = 1f
        } else {
            binding.homeLoginBt.setBackgroundResource(R.drawable.ic_login)
            //binding.homeCreateBt.alpha = 0.3f
            binding.homeLoginBt.text = ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}