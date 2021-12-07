package com.chiki.marsrealestate.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.chiki.marsrealestate.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {

    //Binding
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    //Lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val marsProperty = DetailFragmentArgs.fromBundle(requireArguments()).marsProperty
        val viewModelFactory = DetailViewModelFactory(marsProperty)
        val detailViewModel = ViewModelProvider(this,viewModelFactory).get(DetailViewModel::class.java)

        binding.lifecycleOwner = this
        binding.detailViewModel = detailViewModel
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}