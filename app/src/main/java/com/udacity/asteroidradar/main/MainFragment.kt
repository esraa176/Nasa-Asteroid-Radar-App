package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var binding: FragmentMainBinding
    private lateinit var asteroidAdapter: AsteroidAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        // Navigation (clickListener variable)
        asteroidAdapter = AsteroidAdapter { asteroid ->
            this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        }

        setUpRecyclerView()

        viewModel.asteroidsUpdateLiveData.observe(viewLifecycleOwner)
        {
            //Log.i("observer", "onCreateView: ")
            viewModel.asteroids.observe(
                viewLifecycleOwner
            ) {
                if (it.isEmpty()) {
                    binding.statusLoadingWheel.isVisible = true
                    binding.asteroidRecycler.isVisible = false
                } else {
                    asteroidAdapter.setAsteroids(it)
                    asteroidAdapter.notifyDataSetChanged()
                    binding.statusLoadingWheel.isVisible = false
                    binding.asteroidRecycler.isVisible = true
                }
            }
        }
        return binding.root
    }

    private fun setUpRecyclerView() {
        binding.asteroidRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.asteroidRecycler.adapter = asteroidAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.show_all_menu -> ApiFilter.SHOW_WEEK
                R.id.show_rent_menu -> ApiFilter.SHOW_TODAY
                else -> ApiFilter.SHOW_SAVED
            }
        )
        return true
    }
}
