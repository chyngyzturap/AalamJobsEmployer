package com.pharos.aalamjobsemployer.ui.talents.search.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pharos.aalamjobsemployer.data.model.Talents
import com.pharos.aalamjobsemployer.data.responses.dialog.CountryResponseItem
import com.pharos.aalamjobsemployer.databinding.ListItemBinding

class CountryAdapter(
    private val listener: CountryClickListener
) : ListAdapter<CountryResponseItem, CountryAdapter.JobsViewHolder>(DIFF) {

    private var _binding: ListItemBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        _binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobsViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun getItemAtPos(position: Int): CountryResponseItem {
        return getItem(position)
    }

    inner class JobsViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            val current = getItemAtPos(position)
            binding.tvCounter.text = current.name.en
            binding.root.setOnClickListener {
                listener.onCountryClick(position)
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CountryResponseItem>() {
            override fun areItemsTheSame(
                oldItem: CountryResponseItem,
                newItem: CountryResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CountryResponseItem,
                newItem: CountryResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface CountryClickListener {
        fun onCountryClick(position: Int)
    }
}