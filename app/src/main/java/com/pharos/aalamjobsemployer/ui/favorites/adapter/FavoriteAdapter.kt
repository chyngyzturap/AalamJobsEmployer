package com.pharos.aalamjobsemployer.ui.favorites.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.databinding.TalentsItemBinding
import com.pharos.aalamjobsemployer.ui.favorites.model.FavoriteTalentsItem

class FavoriteAdapter(
    private val listener: TalentClickListener
) : ListAdapter<FavoriteTalentsItem, FavoriteAdapter.JobsViewHolder>(DIFF) {

    private var _binding: TalentsItemBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        _binding = TalentsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobsViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun getItemAtPos(position: Int): FavoriteTalentsItem {
        return getItem(position)
    }

    inner class JobsViewHolder(private val binding: TalentsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(position: Int) {
            val current = getItemAtPos(position)

                binding.talentFullName.text = current.resume.firstname + " " + current.resume.lastname
                binding.talentPosition.text = current.resume.position
                binding.talentLocation.text = current.resume.current_city + ", " +
                        current.resume.current_country
                binding.talentDate.text = current.resume.created_at.split("T")[0]

            if (current.resume.photo != "")
                Glide.with(binding.root).load(current.resume.photo)
                    .into(binding.ivTalentPhoto)

            if (current.resume.favorite){
                binding.ivTalentFavorite.setImageResource(R.drawable.vector_favorite_red_filled)

            } else {
                binding.ivTalentFavorite.setImageResource(R.drawable.vector_favorite_red_empty)
            }

            binding.ivTalentFavorite.setOnClickListener {
                current.resume.favorite
                if (!current.resume.favorite) {
                    current.resume.favorite
                    binding.ivTalentFavorite.setImageResource(R.drawable.vector_favorite_red_filled)
                    listener.addToFavorites(current.id)
                } else {
                    !current.resume.favorite
                    binding.ivTalentFavorite.setImageResource(R.drawable.vector_favorite_red_empty)
                    listener.deleteFromFavorites(current.resume.id)
                }
            }

            binding.root.setOnClickListener {
                listener.onTalentClick(current.resume.id)
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<FavoriteTalentsItem>() {
            override fun areItemsTheSame(oldItem: FavoriteTalentsItem, newItem: FavoriteTalentsItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: FavoriteTalentsItem, newItem: FavoriteTalentsItem): Boolean {
                return oldItem.resume.id == newItem.resume.id
            }
        }
    }

    interface TalentClickListener {
        fun onTalentClick(jobId: Int)
        fun addToFavorites(position: Int)
        fun deleteFromFavorites(position: Int)
    }
}