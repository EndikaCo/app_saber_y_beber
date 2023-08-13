package com.endcodev.saber_y_beber.presenter.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.data.model.PlayersModel
import com.endcodev.saber_y_beber.databinding.FragmentHomeBinding

import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    companion object {
        const val TAG = "HomeFragment ***"
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

        homeViewModel.isConnected.observe(viewLifecycleOwner) {
            setLoginButton(it)
        }
    }

    private fun initListeners() {

        binding.homeCreateBt.setOnClickListener {
            correctQuest()
        }

        //Login imageButton click
        binding.homeLoginBt.setOnClickListener {
            toLoginOrProfileFragment()
        }

        //button add player Onclick
        binding.homeAddBt.setOnClickListener {
            addPlayerDialog()
        }

        binding.homeStartBt.setOnClickListener {
            startGame()
        }
    }

    /** navigate to GameFragment if user is connected and mail verified*/
    private fun correctQuest() {
        if (homeViewModel.isConnected())
            findNavController().navigate(R.id.action_homeFragment_to_correctFragment)
        else
            Toast.makeText(context, getString(R.string.need_login), Toast.LENGTH_SHORT).show()
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

    /** change fragment to LoginFragment or ProfileFragment if already logged*/
    private fun toLoginOrProfileFragment() {
        if (homeViewModel.isConnected())
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        else
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    }

    /** change to [HomeFragment]*/
    private fun startGame() {
        if (homeViewModel.playerList.value?.size!! > 2) {
            //MediaPlayer.create(context, R.raw.opening_bottle).start() //opening bottle sound
            findNavController().navigate(R.id.gameFragment)
        } else
            Toast.makeText(
                context,
                requireContext().getText(R.string.error_no_players),
                Toast.LENGTH_SHORT
            ).show()
    }

    /** change login button background if user is connected*/
    private fun setLoginButton(it: Char?) {
        with(binding) {
            if (it != null) {
                homeLoginBt.setBackgroundResource(R.drawable.user_login_button)
                homeLoginBt.text = it.toString()
                homeCreateBt.alpha = 1f
            } else {
                homeLoginBt.setBackgroundResource(R.drawable.ic_login)
                homeLoginBt.text = ""
                homeCreateBt.alpha = 0.3f
            }
        }
    }

    /** close app when back button is pressed*/
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

    override fun onResume() {
        super.onResume()
        homeViewModel.checkLogin()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}