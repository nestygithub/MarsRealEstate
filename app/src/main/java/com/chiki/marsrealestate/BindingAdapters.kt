package com.chiki.marsrealestate

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chiki.marsrealestate.adapters.MarsPropertyAdapter
import com.chiki.marsrealestate.network.MarsProperty
import com.chiki.marsrealestate.overview.MarsApiStatus

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, imageUrl: String?){
    imageUrl?.let {
        val imageUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imageUri)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imageView)
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<MarsProperty>?){
    val adapter = recyclerView.adapter as MarsPropertyAdapter
    adapter.submitListNew(data)
}

@BindingAdapter("marsApiStatus")
fun bindStatus(statusImageView: ImageView, status: MarsApiStatus?){
    when(status){
        MarsApiStatus.LOADING->{
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        MarsApiStatus.ERROR->{
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        MarsApiStatus.DONE->{
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("textType")
fun bindType(textView: TextView,marsProperty: MarsProperty?){
    marsProperty?.let {
        val context = textView.context
        textView.text = context.getString(R.string.display_type,it.type)
    }
}

@BindingAdapter("textPrice")
fun bindPrice(textView: TextView,marsProperty: MarsProperty?){
    marsProperty?.let {
        val context = textView.context
        when(it.isRental){
            true->  textView.text = context.getString(R.string.display_price_monthly_rental,it.price)
            else->  textView.text = context.getString(R.string.display_price,it.price)
        }
    }
}