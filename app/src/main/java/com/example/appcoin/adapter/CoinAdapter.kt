package com.example.appcoin.adapter

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appcoin.R
import com.example.appcoin.models.Coin
import kotlinx.android.synthetic.main.item_list_coin.view.*
import java.net.URL

class CoinAdapter(var items: List<Coin>,
                  var listener: (Coin) -> Unit):
    RecyclerView.Adapter<CoinAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_coin,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position],listener)
    }

    fun setData(items:List<Coin>) {
        this.items =  items
        notifyDataSetChanged()
    }

    class ViewHolder(var item: View): RecyclerView.ViewHolder(item){
        fun bind(coin:Coin,listener: (Coin) -> Unit){
            with(item){
                tv_name.text = coin.name
                tv_country.text = coin.country
                tv_year.text = coin.year.toString()
                setOnClickListener {
                    listener(coin)
                }
            }
        }
    }


}