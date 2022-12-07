package com.viamedsalud.gvp.ui.episodio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.viamedsalud.gvp.databinding.RowEpisodioBinding
import com.viamedsalud.gvp.handler.EpisodioHandler
import com.viamedsalud.gvp.model.Episodio

class EpisodioListAdapter(private val episodioHandler: EpisodioHandler) :
    RecyclerView.Adapter<EpisodioListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpisodioListAdapter.ViewHolder {
        val binding =
            RowEpisodioBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.handler = this
        binding.executePendingBindings()

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodioListAdapter.ViewHolder, position: Int) {
        holder.bind(episodios[position])

    }

    override fun getItemCount(): Int {
        return episodios.size
    }

    inner class ViewHolder(val binding: RowEpisodioBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(episodio: Episodio) {
            binding.episodio = episodio
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Episodio>() {
        override fun areItemsTheSame(oldItem: Episodio, newItem: Episodio): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Episodio, newItem: Episodio): Boolean {
            return newItem == oldItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var episodios: List<Episodio>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }


    fun updatePacientes(episodios: List<Episodio>) {
        this.episodios = episodios
        notifyDataSetChanged()
    }

    fun onEpisodioClicked(episodio: String) {
        episodioHandler.onEpisodioClicked(episodio = episodio)
    }

}