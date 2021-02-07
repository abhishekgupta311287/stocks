package com.abhishekgupta.stocks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.abhishekgupta.stocks.R
import com.abhishekgupta.stocks.databinding.StocksActivityBinding
import com.abhishekgupta.stocks.databinding.StocksFragmentBinding
import com.abhishekgupta.stocks.viewmodel.StocksViewModel

class StocksFragment : Fragment() {

    companion object {
        fun newInstance() = StocksFragment()
    }

    private var _binding: StocksFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: StocksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StocksFragmentBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StocksViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}