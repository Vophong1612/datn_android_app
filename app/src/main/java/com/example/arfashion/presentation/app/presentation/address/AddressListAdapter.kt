package com.example.arfashion.presentation.app.presentation.address

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arfashion.R
import com.example.arfashion.presentation.app.models.address.AddressResponse
import com.example.arfashion.presentation.app.presentation.payment.PaymentActivity


class AddressListAdapter(private val context: Activity) :
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
        private val ivChoose = view.findViewById<ImageView>(R.id.iv_choose_address)
        private val tvDefault = view.findViewById<TextView>(R.id.receiver_default)

        init {

            ivChoose.setOnClickListener {
                PaymentActivity.paymentName = addressList[adapterPosition].name
                PaymentActivity.paymentEmail = addressList[adapterPosition].email
                PaymentActivity.paymentPhone = addressList[adapterPosition].phone
                PaymentActivity.paymentAddress =
                    addressList[adapterPosition].home +  ", " +  addressList[adapterPosition].village.name  + ", " +  addressList[adapterPosition].district.name  + ", " +  addressList[adapterPosition].province.name
                PaymentActivity.paymentDefault = addressList[adapterPosition].isDefault
                context.finish()
            }

            itemView.setOnClickListener {
                addressClickLister?.invoke(addressList[adapterPosition]._id)
                val intent = Intent(context, AddNewAddressActivity::class.java)
                intent.putExtra("mode", "edit")
                intent.putExtra("obj.id", addressList[adapterPosition]._id)
                intent.putExtra("obj.name", addressList[adapterPosition].name)
                intent.putExtra("obj.email", addressList[adapterPosition].email)
                intent.putExtra("obj.phone", addressList[adapterPosition].phone)
                intent.putExtra("obj.home", addressList[adapterPosition].home)
                intent.putExtra("obj.village", addressList[adapterPosition].village.name)
                intent.putExtra("obj.villageCode", addressList[adapterPosition].village.code)
                intent.putExtra("obj.district", addressList[adapterPosition].district.name)
                intent.putExtra("obj.districtCode", addressList[adapterPosition].district.code)
                intent.putExtra("obj.province", addressList[adapterPosition].province.name)
                intent.putExtra("obj.provinceCode", addressList[adapterPosition].province.code)
                intent.putExtra("obj.isDefault", addressList[adapterPosition].isDefault.toString())
                context.startActivity(intent)
            }

            itemView.setOnLongClickListener {
                addressClickLister?.invoke(addressList[adapterPosition]._id)
                displayDialog(addressList[adapterPosition])
            }

        }

        @SuppressLint("SetTextI18n")
        fun bindData(res: AddressResponse) {
            name.text = res.name
            email.text = res.email
            phone.text = res.phone
            address.text = res.home + ", " + res.village.name  + ", " + res.district.name  + ", " + res.province.name
            if(res.isDefault) tvDefault.visibility = View.VISIBLE
            else tvDefault.visibility = View.INVISIBLE
        }
    }

    fun displayDialog(item: AddressResponse): Boolean {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        builder.setTitle("Confirm")
        builder.setMessage("Are you sure to delete address?")

        builder.setPositiveButton(
            "YES"
        ) { dialog, _ -> // Do nothing but close the dialog
            dialog.dismiss()
            AddressListActivity.user.credential.accessToken?.let {
                AddressListActivity.addressViewModel.deleteAddress(
                    it,
                    item._id
                )
                addressList.remove(item)
                notifyDataSetChanged()
            }
        }

        builder.setNegativeButton(
            "NO"
        ) { dialog, _ -> // Do nothing
            dialog.dismiss()
        }

        val alert: AlertDialog = builder.create()
        alert.show()
        return true
    }
}