package com.pharos.aalamjobsemployer.utils.dialogfragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pharos.aalamjobsemployer.data.responses.dialog.EmploymentTypeResponseItem
import com.pharos.aalamjobsemployer.databinding.ListItemBinding

class EmploymentTypeAdapter(
    private val listener: EmplTypeClickListener
) : ListAdapter<EmploymentTypeResponseItem, EmploymentTypeAdapter.JobsViewHolder>(DIFF) {

    private var _binding: ListItemBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        _binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobsViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun getItemAtPos(position: Int): EmploymentTypeResponseItem {
        return getItem(position)
    }

    inner class JobsViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            val current = getItemAtPos(position)
            binding.tvCounter.text = current.name.en
            binding.root.setOnClickListener {
                listener.onEmplTypeClick(position)
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<EmploymentTypeResponseItem>() {
            override fun areItemsTheSame(
                oldItem: EmploymentTypeResponseItem,
                newItem: EmploymentTypeResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: EmploymentTypeResponseItem,
                newItem: EmploymentTypeResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface EmplTypeClickListener {
        fun onEmplTypeClick(position: Int)
    }
}