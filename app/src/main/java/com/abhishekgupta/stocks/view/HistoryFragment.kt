package com.abhishekgupta.stocks.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.abhishekgupta.stocks.R
import com.abhishekgupta.stocks.databinding.HistoryFragmentBinding
import com.abhishekgupta.stocks.viewmodel.StocksViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class HistoryFragment : Fragment() {

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private var _binding: HistoryFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by sharedViewModel<StocksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HistoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.historyLiveData.observe(viewLifecycleOwner, {

            val latestStock = it[it.size-1]
            binding.sidPrice.text = latestStock.price.toString()
            binding.absoluteChange.text = latestStock.change.toString()

            binding.percentChange.text = getString(
                R.string.chart_percent_label,
                String.format("%.2f", latestStock.change * 100 / latestStock.price)
            )

            val isChangePositive = latestStock.change > 0
            binding.sidChangeIcon.setImageResource(
                if (isChangePositive) {
                    binding.absoluteChange.setTextColor(Color.GREEN)
                    R.drawable.green_stock_up_icon
                } else {
                    binding.absoluteChange.setTextColor(Color.RED)
                    R.drawable.red_stock_down_icon
                }
            )


            var x = 0.0f
            val entries: List<Entry> = it.map { stock ->
                x += 5.0f
                Entry(x, stock.price)
            }

            val dataSet = getDataSet(entries, isChangePositive)

            binding.lineChart.apply {

                if (it.size > 1) {
                    data = LineData(dataSet)
                }
                description.isEnabled = false
                setScaleEnabled(false)
                animateX(1500)
                xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                    setDrawLabels(false)
                    position = XAxis.XAxisPosition.BOTTOM
                }
                axisLeft.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                    labelCount = 10
                }
                axisRight.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    setDrawLabels(false)
                }
            }

        })

    }

    private fun getDataSet(entries: List<Entry>, isChangePositive: Boolean): LineDataSet {
        return LineDataSet(entries, getString(R.string.chart_price_label)).apply {
            setDrawCircles(false)
            setDrawFilled(true)
            color = if (isChangePositive) {
                Color.GREEN
            } else {
                Color.RED
            }
            fillColor = color
            fillAlpha = 15

            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0.2f
            lineWidth = 1.8f
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}