package com.example.arfashion.presentation.app.presentation.address

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.arfashion.R
import com.example.arfashion.presentation.app.models.address.AddressResponse
import java.io.Serializable


class AddressListAdapter(private val context: Context, private val isViewPager: Boolean) :
    RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {

    private var addressList: MutableList<AddressResponse> = mutableListOf()

    var addressClickLister: ((addressId: String) -> Unit)? = null

    fun setAddressList(addressList: List<AddressResponse>?) {
        this.addressList.clear()
        addressList?.let {
            this.addressList.addAll(addressList)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(addressList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.receiver_name)
        private val email = view.findViewById<TextView>(R.id.receiver_email)
        private val phone = view.findViewById<TextView>(R.id.receiver_phone)
        private val address = view.findViewById<TextView>(R.id.receiver_address)

        init {
            if (isViewPager) {
                view.layoutParams = ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            itemView.setOnClickListener {
                addressClickLister?.invoke(addressList[adapterPosition]._id)
                val intent = Intent(context, AddNewAddressActivity::class.java)
                intent.putExtra("mode", "edit")
                intent.putExtra("obj", addressList[adapterPosition] as Serializable)
                context.startActivity(intent)
            }

            itemView.setOnLongClickListener {
                addressClickLister?.invoke(addressList[adapterPosition]._id)
                displayDialog(addressList[adapterPosition])
            }
        }

        fun bindData(res: AddressResponse) {

            name.text = res.name
            email.text = res.email
            phone.text = res.phone
            address.text = res.home + ", " + res.village  + ", " + res.district  + ", " + res.province

        }
    }

    fun displayDialog(item: AddressResponse): Boolean {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        builder.setTitle("Confirm")
        builder.setMessage("Are you sure to delete address?")

        builder.setPositiveButton(
            "YES",
            DialogInterface.OnClickListener { dialog, which -> // Do nothing but close the dialog
                dialog.dismiss()
                AddNewAddressActivity.userManager.currentUser.credential.accessToken?.let {
                    AddNewAddressActivity.addressViewModel.deleteAddress(
                        it,
                        item._id)
                }
            })

        builder.setNegativeButton(
            "NO",
            DialogInterface.OnClickListener { dialog, which -> // Do nothing
                dialog.dismiss()
            })

        val alert: AlertDialog = builder.create()
        alert.show()
        return true
    }
}