package com.abhishekgupta.stocks.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhishekgupta.stocks.R
import com.abhishekgupta.stocks.databinding.StocksFragmentBinding
import com.abhishekgupta.stocks.model.Resource
import com.abhishekgupta.stocks.view.adapter.StocksAdapter
import com.abhishekgupta.stocks.viewmodel.StocksViewModel
import org.koin.android.ext.android.get

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
        setHasOptionsMenu(true)
        _binding = StocksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = get()

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.history -> {

            }
            R.id.play ->{

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}