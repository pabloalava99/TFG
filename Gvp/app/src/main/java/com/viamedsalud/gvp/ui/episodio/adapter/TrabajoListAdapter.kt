package com.viamedsalud.gvp.ui.episodio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.viamedsalud.gvp.databinding.RowTrabajoBinding
import com.viamedsalud.gvp.model.Trabajo

class TrabajoListAdapter() : RecyclerView.Adapter<TrabajoListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrabajoListAdapter.ViewHolder {
        val binding =
            RowTrabajoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.handler = this
        binding.executePendingBindings()

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrabajoListAdapter.ViewHolder, position: Int) {
        holder.bind(trabajo[position])
    }

    override fun getItemCount(): Int {
        return trabajo.size
    }

    inner class ViewHolder(val binding: RowTrabajoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trabajo: Trabajo) {
            binding.trabajo = trabajo
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Trabajo>() {
        override fun areItemsTheSame(oldItem: Trabajo, newItem: Trabajo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Trabajo, newItem: Trabajo): Boolean {
            return newItem == oldItem
        }
    }


    private val differ = AsyncListDiffer(this, diffCallback)
    var trabajo: List<Trabajo>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }


    fun updateTrabajo(trabajo: List<Trabajo>) {
        this.trabajo = trabajo
        notifyDataSetChanged()
    }


}