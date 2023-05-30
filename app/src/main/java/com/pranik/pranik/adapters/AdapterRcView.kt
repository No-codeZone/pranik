package com.pranik.pranik.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pranik.pranik.R
import com.pranik.pranik.fragment.HomeFragment
import com.pranik.pranik.modal.Result

class AdapterRcView(private val list: ArrayList<Result>, var listener :  OnItemClickListener, val context: HomeFragment):
    RecyclerView.Adapter<AdapterRcView.ViewHolder>(){

    interface OnItemClickListener {
        fun alertDialog(position: Int, list: ArrayList<Result>)
    }


    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val imageButton:ImageButton=itemView.findViewById(R.id.imgBtnEdit)
        val uid:TextView=itemView.findViewById(R.id.userID)
        val lat:TextView=itemView.findViewById(R.id.userLat)
        val lon:TextView=itemView.findViewById(R.id.userLong)
        val range:TextView=itemView.findViewById(R.id.userRange)
        val status:TextView=itemView.findViewById(R.id.userActivityStatus)
        val title:TextView=itemView.findViewById(R.id.userTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.rcview_userlist,parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem=list[holder.bindingAdapterPosition]
        Log.e("TAG", "onBindViewHolder:dadasdas ${currentItem.Id}")
        holder.apply {
            uid.text=currentItem.Id.toString()
            lat.text=currentItem.Latitude.toString()
            lon.text=currentItem.Longitude.toString()
            range.text=currentItem.Range.toString()
            status.text=currentItem.IsActive.toString()
            title.text=currentItem.Title.toString()
            imageButton.setOnClickListener {
                listener.alertDialog(holder.bindingAdapterPosition, list)
            }
        }
     }

//    interface BtnClickListener {
//        fun onBtnClick(position: Int)
//    }
}