package com.pharos.aalamjobsemployer.ui.vacancies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pharos.aalamjobsemployer.data.local.db.cv.models.JobModel
import com.pharos.aalamjobsemployer.data.local.db.cv.models.JobModelResponse
import com.pharos.aalamjobsemployer.data.local.db.cv.models.JobModelResponseItem
import com.pharos.aalamjobsemployer.databinding.ResumesItemBinding

class VacancyAdapter(
    private val listener: CvClickListener
) : ListAdapter<JobModelResponseItem, VacancyAdapter.CvViewHolder>(DIFF) {

    private var _binding: ResumesItemBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CvViewHolder {
        _binding = ResumesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CvViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: CvViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun getItemAtPos(position: Int): JobModelResponseItem {
        return getItem(position)
    }

    inner class CvViewHolder(private val binding: ResumesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            val current = getItemAtPos(position)
            binding.tvVacancyTitle.text = current.title
            binding.tvDate.text = current.published_date.split("T")[0]
            binding.root.setOnClickListener {
                listener.onCvClick(current.id)
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<JobModelResponseItem>() {
            override fun areItemsTheSame(
                oldItem: JobModelResponseItem,
                newItem: JobModelResponseItem
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: JobModelResponseItem,
                newItem: JobModelResponseItem
            ): Boolean {
                return oldItem.position == newItem.position
            }
        }
    }

    interface CvClickListener {
        fun onCvClick(cvId: Int)
    }
}