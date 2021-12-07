package com.chiki.marsrealestate.overview

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chiki.marsrealestate.R
import com.chiki.marsrealestate.adapters.DataItem
import com.chiki.marsrealestate.adapters.MarsPropertyAdapter
import com.chiki.marsrealestate.databinding.FragmentOverViewBinding
import com.chiki.marsrealestate.databinding.GridViewItemBinding
import com.chiki.marsrealestate.network.MarsProperty


class OverViewFragment : Fragment() {
    //ViewModels
    private val overViewViewModel: OverViewViewModel by lazy {
        ViewModelProvider(this).get(OverViewViewModel::class.java)
    }

    //Binding
    private var _binding: FragmentOverViewBinding? = null
    private val binding get() = _binding!!

    //Lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOverViewBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.overViewViewModel = overViewViewModel
        binding.photosGrid.adapter = MarsPropertyAdapter{
            if(it is DataItem.MarsPropertyItem) overViewViewModel.onSelectMarsProperty(it.marsProperty)
        }

        //Observers
        overViewViewModel.selectedMarsProperty.observe(viewLifecycleOwner){
            if(it!=null){
                navigateToDetailFragment(it)
                overViewViewModel.doneNavigateToDetailFragment()
            }
        }

        setHasOptionsMenu(true)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        overViewViewModel.updateFilter(when(item.itemId){
            R.id.show_all_menu->MarsApiFilter.ALL
            R.id.show_rent_menu->MarsApiFilter.RENT
            R.id.show_buy_menu->MarsApiFilter.BUY
            else->MarsApiFilter.ALL
        })
        return super.onOptionsItemSelected(item)
    }

    //Actions
    private fun navigateToDetailFragment(marsProperty: MarsProperty){
        val action = OverViewFragmentDirections.actionOverViewFragmentToDetailFragment(marsProperty)
        findNavController().navigate(action)
    }
}