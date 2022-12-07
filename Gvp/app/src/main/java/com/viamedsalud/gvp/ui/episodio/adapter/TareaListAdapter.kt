package com.viamedsalud.gvp.ui.episodio.adapter

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.orhanobut.logger.Logger
import com.viamedsalud.gvp.R
import com.viamedsalud.gvp.database.entities.TareaEntity

class TareaListAdapter(dataSet: List<TareaEntity> = emptyList())
    : DragDropSwipeAdapter<TareaEntity, TareaListAdapter.ViewHolder>(dataSet) {

    class ViewHolder(TareaEntityLayout: View) : DragDropSwipeAdapter.ViewHolder(TareaEntityLayout) {
        val tarea: TextView = itemView.findViewById(R.id.titulo)
        val estado: TextView = itemView.findViewById(R.id.estado)
        val icono : ImageView = itemView.findViewById(R.id.icono_tarea)
        val dragIcon: ImageView = itemView.findViewById(R.id.drag_icon)

    }

    override fun getViewHolder(itemView: View): ViewHolder {
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(item: TareaEntity, viewHolder: ViewHolder, position: Int) {

        viewHolder.tarea.text = item.tarea
        viewHolder.estado.text = item.estado
        val colorRojo : Int = Color.parseColor("#E74C3C")
        val colorVerde : Int = Color.parseColor("#2ECC71")

        if(item.estado == "Pendiente"){
            viewHolder.icono.setBackgroundColor(colorRojo)
            viewHolder.estado.setTextColor(colorRojo)
        }else{
            viewHolder.icono.setBackgroundColor(colorVerde)
            viewHolder.estado.setTextColor(colorVerde)
        }

    }

    override fun getViewToTouchToStartDraggingItem(item: TareaEntity, viewHolder: ViewHolder, position: Int) = viewHolder.dragIcon

    override fun onDragStarted(item: TareaEntity, viewHolder: ViewHolder) {
        //Logger.log("Dragging started on ${item.tarea}")
    }

    override fun onSwipeStarted(item: TareaEntity, viewHolder: ViewHolder) {
        //Logger.log("Swiping started on ${item.tarea}")
    }


}