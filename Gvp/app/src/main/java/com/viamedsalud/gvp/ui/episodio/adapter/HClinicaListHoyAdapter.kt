package com.viamedsalud.gvp.ui.episodio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import com.viamedsalud.gvp.databinding.RowVisitasHoyBinding
import com.viamedsalud.gvp.model.Episodio
import com.viamedsalud.gvp.model.HClinica
import java.text.SimpleDateFormat
import java.util.*

class HClinicaListHoyAdapter() : RecyclerView.Adapter<HClinicaListHoyAdapter.ViewHolder>() {

    var size = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HClinicaListHoyAdapter.ViewHolder {
        val binding =
            RowVisitasHoyBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.handler = this
        binding.executePendingBindings()

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HClinicaListHoyAdapter.ViewHolder, position: Int) {
            holder.bind(hclinica[position])
    }

    override fun getItemCount(): Int {
        return hclinica.size
    }

    inner class ViewHolder(val binding: RowVisitasHoyBinding) :
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

    fun updateList(hclinica: HClinica) {
        this.hclinica = this.hclinica + hclinica
        notifyDataSetChanged()
    }

}