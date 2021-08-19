package com.pharos.aalamjobsemployer.ui.talents.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.data.model.Talents
import com.pharos.aalamjobsemployer.databinding.TalentsItemBinding

class TalentsAdapter(
    private val listener: TalentClickListener
) : ListAdapter<Talents, TalentsAdapter.JobsViewHolder>(DIFF) {

    private var _binding: TalentsItemBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        _binding = TalentsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobsViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun getItemAtPos(position: Int): Talents {
        return getItem(position)
    }

    inner class JobsViewHolder(private val binding: TalentsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(position: Int) {
            val current = getItemAtPos(position)

            binding.talentFullName.text = current.firstname + " " + current.lastname
            binding.talentPosition.text = current.position
            binding.talentLocation.text = current.current_city + ", " +
                    current.current_country
            binding.talentDate.text = current.created_at.split("T")[0]
            binding.jobsSalary.text = current.salary.toString() + current.currency.sign

            if (current.photo != "")
                Glide.with(binding.root).load(current.photo)
                    .into(binding.ivTalentPhoto)

            if (current.favorite) {
                binding.ivTalentFavorite.setImageResource(R.drawable.vector_favorite_red_filled)
            } else {
                binding.ivTalentFavorite.setImageResource(R.drawable.vector_favorite_red_empty)
            }

            binding.ivTalentFavorite.setOnClickListener {
                current.favorite

                if (!current.favorite) {
                    current.favorite
                    binding.ivTalentFavorite.setImageResource(R.drawable.vector_favorite_red_filled)
                    listener.addToFavorites(current.id)
                } else {
                    !current.favorite
                    binding.ivTalentFavorite.setImageResource(R.drawable.vector_favorite_red_empty)
                    listener.deleteFromFavorites(current.id)
                }
            }

            binding.root.setOnClickListener {
                listener.onTalentClick(current.id)
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Talents>() {
            override fun areItemsTheSame(oldItem: Talents, newItem: Talents): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Talents, newItem: Talents): Boolean {
                return oldItem.firstname == newItem.firstname
            }
        }
    }

    interface TalentClickListener {
        fun onTalentClick(jobId: Int)
        fun addToFavorites(position: Int)
        fun deleteFromFavorites(position: Int)
    }
}