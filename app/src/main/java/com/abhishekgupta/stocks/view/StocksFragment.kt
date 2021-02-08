package com.abhishekgupta.stocks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhishekgupta.stocks.databinding.StocksFragmentBinding
import com.abhishekgupta.stocks.model.Resource
import com.abhishekgupta.stocks.view.adapter.StocksAdapter
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
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StocksViewModel::class.java)

        val adapter = StocksAdapter()

        binding.stockRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.stockRecyclerView.adapter = adapter

        binding.stockRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.stockRecyclerView.context,
                LinearLayoutManager.VERTICAL
            )
        )

        viewModel.stocksLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { stocks ->
                        adapter.stocks = stocks
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        })

        viewModel.getStockQuotes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}