package com.pharos.aalamjobsemployer.ui.talents.search.sector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pharos.aalamjobsemployer.data.responses.dialog.SpecResponseItem
import com.pharos.aalamjobsemployer.databinding.ListItemBinding

class SpecAdapter(
    private val listener: SectorClickListener
) : ListAdapter<SpecResponseItem, SpecAdapter.JobsViewHolder>(DIFF) {

    private var _binding: ListItemBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        _binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobsViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun getItemAtPos(position: Int): SpecResponseItem {
        return getItem(position)
    }

    inner class JobsViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            val current = getItemAtPos(position)
            binding.tvCounter.text = current.name
            binding.root.setOnClickListener {
                listener.onSectorClick(position)
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<SpecResponseItem>() {
            override fun areItemsTheSame(
                oldItem: SpecResponseItem,
                newItem: SpecResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SpecResponseItem,
                newItem: SpecResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface SectorClickListener {
        fun onSectorClick(position: Int)
    }
}