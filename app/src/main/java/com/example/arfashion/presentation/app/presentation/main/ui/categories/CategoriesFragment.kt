package com.example.arfashion.presentation.app.presentation.main.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.arfashion.R

class CategoriesFragment : Fragment() {
    companion object {
        const val TAG = "DashboardFragment"

        fun newInstance(): CategoriesFragment {
            val args = Bundle()

            val fragment = CategoriesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var categoriesViewModel: CategoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
//        val textView: TextView = root.findViewById(R.id.text_dashboard)
//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }
}