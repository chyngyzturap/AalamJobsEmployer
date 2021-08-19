package com.pharos.aalamjobsemployer.ui.talents.search.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pharos.aalamjobsemployer.data.responses.dialog.CityResponseItem
import com.pharos.aalamjobsemployer.databinding.ListItemBinding

class CityAdapter(
    private val listener: CityClickListener
) : ListAdapter<CityResponseItem, CityAdapter.JobsViewHolder>(DIFF) {

    private var _binding: ListItemBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        _binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobsViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun getItemAtPos(position: Int): CityResponseItem {
        return getItem(position)
    }

    inner class JobsViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            val current = getItemAtPos(position)
            binding.tvCounter.text = current.name.en
            binding.root.setOnClickListener {
                listener.onCityClick(position)
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CityResponseItem>() {
            override fun areItemsTheSame(
                oldItem: CityResponseItem,
                newItem: CityResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CityResponseItem,
                newItem: CityResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface CityClickListener {
        fun onCityClick(position: Int)
    }
}