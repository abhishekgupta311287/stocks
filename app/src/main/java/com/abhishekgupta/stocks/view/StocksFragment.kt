package com.abhishekgupta.stocks.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.abhishekgupta.stocks.R
import com.abhishekgupta.stocks.databinding.StocksFragmentBinding
import com.abhishekgupta.stocks.model.Resource
import com.abhishekgupta.stocks.model.Stock
import com.abhishekgupta.stocks.view.adapter.StocksAdapter
import com.abhishekgupta.stocks.viewmodel.StocksViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class StocksFragment : Fragment(), IStockListener {

    companion object {
        fun newInstance() = StocksFragment()
    }

    private var _binding: StocksFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by sharedViewModel<StocksViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }

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

        val adapter = StocksAdapter(this)

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
                    successUI()
                }
                is Resource.Error -> {
                    if (!viewModel.isPolling) {
                        errorUI()
                    }
                }
                is Resource.Loading -> {
                    if (!viewModel.isPolling) {
                        loadingUI()
                    }
                }
            }
        })

        viewModel.getStockQuotes()
    }

    private fun loadingUI() {
        binding.stockRecyclerView.visibility = View.INVISIBLE
        binding.errorView.visibility = View.INVISIBLE
        binding.shimmerLayout.root.visibility = View.VISIBLE
        binding.shimmerLayout.root.showShimmer(true)
    }

    private fun errorUI() {
        binding.stockRecyclerView.visibility = View.INVISIBLE
        binding.errorView.visibility = View.VISIBLE
        binding.shimmerLayout.root.visibility = View.INVISIBLE
        binding.shimmerLayout.root.hideShimmer()
    }

    private fun successUI() {
        binding.stockRecyclerView.visibility = View.VISIBLE
        binding.errorView.visibility = View.INVISIBLE
        binding.shimmerLayout.root.visibility = View.INVISIBLE
        binding.shimmerLayout.root.hideShimmer()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        menu.findItem(R.id.play).setIcon(
            if (viewModel.isPolling) {
                R.drawable.pause_menu_icon
            } else {
                R.drawable.play_menu_icon
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.history -> {
                viewModel.fetchExpensiveStockHistory()
            }
            R.id.play -> {
                if (viewModel.isPolling) {
                    viewModel.stopPolling()
                    item.setIcon(R.drawable.play_menu_icon)
                } else {
                    viewModel.pollStockQuotes()
                    item.setIcon(R.drawable.pause_menu_icon)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStockSelected(stock: Stock) {
        viewModel.fetchStockHistory(stock)
    }

}