package com.chiki.marsrealestate.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chiki.marsrealestate.databinding.GridViewItemBinding
import com.chiki.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_ITEM = 1

class MarsPropertyAdapter(private val onItemClicked: (DataItem)->Unit): ListAdapter<DataItem, RecyclerView.ViewHolder>(DiffCallback) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_TYPE_ITEM -> MarsPropertyViewHolder.from(this,parent,onItemClicked)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is MarsPropertyViewHolder -> {
                val propertyItem = getItem(position) as DataItem.MarsPropertyItem
                holder.bind(propertyItem.marsProperty)
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is DataItem.MarsPropertyItem -> ITEM_VIEW_TYPE_ITEM
        }
    }
    fun submitListNew(list: List<MarsProperty>?){
        adapterScope.launch {
            val items = list?.map { DataItem.MarsPropertyItem(it)}
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }


    class MarsPropertyViewHolder private constructor(private val binding:GridViewItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(marsProperty: MarsProperty) {
            binding.property = marsProperty
            binding.executePendingBindings()
        }
        companion object{
            fun from(marsPropertyAdapter: MarsPropertyAdapter, parent: ViewGroup, onItemClicked: (DataItem) -> Unit): MarsPropertyViewHolder {
                val viewHolder = MarsPropertyViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))
                viewHolder.itemView.setOnClickListener {
                    val position = viewHolder.adapterPosition
                    onItemClicked(marsPropertyAdapter.getItem(position))
                }
                return viewHolder
            }
        }
    }

    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
sealed class DataItem{
    abstract val id: String

    data class MarsPropertyItem(val marsProperty: MarsProperty): DataItem(){
        override val id = marsProperty.id
    }
}
