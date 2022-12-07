package com.viamedsalud.gvp.ui.episodio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.viamedsalud.gvp.databinding.RowEpisodioBinding
import com.viamedsalud.gvp.databinding.RowHclinicaBinding
import com.viamedsalud.gvp.model.Episodio
import com.viamedsalud.gvp.model.HClinica

class HClinicaListAdapter() : RecyclerView.Adapter<HClinicaListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HClinicaListAdapter.ViewHolder {
        val binding =
            RowHclinicaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.handler = this
        binding.executePendingBindings()

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HClinicaListAdapter.ViewHolder, position: Int) {
        holder.bind(hclinica[position])
    }

    override fun getItemCount(): Int {
        return hclinica.size
    }

    inner class ViewHolder(val binding: RowHclinicaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hclinica: HClinica) {
            binding.hclinica = hclinica
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<HClinica>() {
        override fun areItemsTheSame(oldItem: HClinica, newItem: HClinica): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HClinica, newItem: HClinica): Boolean {
            return newItem == oldItem
        }
    }


    private val differ = AsyncListDiffer(this, diffCallback)
    var hclinica: List<HClinica>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }


    fun updateHClinica(hclinica: List<HClinica>) {
        this.hclinica = hclinica
        notifyDataSetChanged()
    }


}