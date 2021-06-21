package com.example.arfashion.presentation.app.presentation.bill
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arfashion.R
import com.example.arfashion.presentation.app.MyViewModelFactory
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.visible
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Bill
import kotlinx.android.synthetic.main.fragment_bill.*
import kotlinx.android.synthetic.main.fragment_bill.refreshLayout
private const val KEY_STATUS_ID = "key_bundle_status_id"
private const val KEY_NAME_STATUS = "key_bundle_name_status"
class BillFragment : Fragment() {
    companion object {
        fun newInstance(statusId: String, name: String): BillFragment {
            return BillFragment().apply {
                arguments = bundleOf(
                    KEY_STATUS_ID to statusId,
                    KEY_NAME_STATUS  to name
                )
            }
        }
    }
    private var statusId: String? = null
    private var nameStatus: String = ""
    private var offset = 0
    private var bills: MutableList<Bill> = mutableListOf()
    private lateinit var billViewModel: BillViewModel
    private lateinit var billAdapter: BillAdapter
    private lateinit var appContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bill, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statusId = arguments?.getString(KEY_STATUS_ID)
        nameStatus = arguments?.getString(KEY_NAME_STATUS)?: ""
        appContext = context?: requireContext()
        billViewModel = ViewModelProvider(this, MyViewModelFactory(appContext)).get(BillViewModel::class.java)
        getData(offset)
        billAdapter = BillAdapter(appContext)
        with(billList){
            adapter = billAdapter
            layoutManager = LinearLayoutManager(appContext)
            addItemDecoration(BillItemOffset(appContext))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lm = layoutManager as LinearLayoutManager
                    if (lm.findLastCompletelyVisibleItemPosition() == bills.size - 1) {
                        if (!refreshLayout.isRefreshing) {
                            getData(offset)
                        }
                    }
                }
            })
        }
        refreshLayout.setOnRefreshListener {
            offset = 0
            getData(offset)
        }
        billViewModel.loading.observe(viewLifecycleOwner) {
            refreshLayout.isRefreshing = it
        }
        billViewModel.offset.observe(viewLifecycleOwner) {
            when (it) {
                is ARResult.Success -> offset = it.data
                is ARResult.Error -> {
                }
            }
        }
        billViewModel.billList.observe(viewLifecycleOwner) {
            when (it) {
                is ARResult.Success -> handleData(it.data)
                is ARResult.Error -> Toast.makeText(appContext, it.throwable.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun handleData(list: List<Bill>){
        if (list.isEmpty() && offset == 0) {
            hideBillList()
            emptyAlert.text = appContext.getString(R.string.no_bill_by_status, nameStatus)
            return
        }
        showBillList()
        if (offset != 0) {
            billAdapter.addProducts(list)
        } else {
            billAdapter.setData(list)
            bills.clear()
        }
        bills.addAll(list)
    }
    private fun showBillList(){
        billList.visible()
        emptyAlert.gone()
    }
    private fun hideBillList() {
        emptyAlert.visible()
        billList.gone()
    }
    private fun getData(offset: Int) {
        statusId?.let {
            billViewModel.getBillListByStatus(it, offset)
        }
    }
}