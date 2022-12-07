package com.viamedsalud.gvp.ui.episodio

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import com.shashank.sony.fancytoastlib.FancyToast
import com.viamedsalud.gvp.ui.episodio.adapter.TareaListAdapter
import com.viamedsalud.gvp.R
import com.viamedsalud.gvp.database.entities.TareaEntity
import com.viamedsalud.gvp.databinding.FragmentTareaListBinding
import com.viamedsalud.gvp.model.Tarea
import com.viamedsalud.gvp.ui.episodio.viewmodel.TareaViewModel
import com.viamedsalud.gvp.util.getStatusBarHeight
import com.viamedsalud.gvp.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TareaListFragment : Fragment() {

    lateinit var binding: FragmentTareaListBinding

    lateinit var tareasPendientesListAdapter: TareaListAdapter
    lateinit var tareasCompletadasAdapter: TareaListAdapter

    private val tareaViewModel: TareaViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_tarea_list, container, false)

        toolbarConfig()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listarPendientes()
        listarCompletados()

        bindObserver()
    }

    private fun bindObserver(){
        tareaViewModel.allTareas.observe(requireActivity()) { listTareas ->
            tareasPendientesListAdapter.dataSet = listTareas.filter { it.estado == "Pendiente" }
            tareasCompletadasAdapter.dataSet = listTareas.filter { it.estado != "Pendiente" }
        }
    }


    private fun listarPendientes() {

        tareasPendientesListAdapter = TareaListAdapter()
        val mList: DragDropSwipeRecyclerView = binding.list
        mList.layoutManager = LinearLayoutManager(context)
        mList.adapter = tareasPendientesListAdapter

        mList.orientation =
            DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING
        mList.reduceItemAlphaOnSwiping = false
        mList.disableSwipeDirection(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.RIGHT)

        val onItemSwipeListener = object : OnItemSwipeListener<TareaEntity> {
            override fun onItemSwiped(
                position: Int,
                direction: OnItemSwipeListener.SwipeDirection,
                item: TareaEntity
            ): Boolean {
                when (direction) {
                    //Completar Tarea
                    OnItemSwipeListener.SwipeDirection.RIGHT_TO_LEFT -> {
                        tareaViewModel.updateTarea("Completado",item.id)
                    }
                    else -> return false
                }
                return false
            }
        }
        mList.swipeListener = onItemSwipeListener

        //Boton Añadir
        fabAddItem()
    }

    private fun listarCompletados() {

        tareasCompletadasAdapter = TareaListAdapter()
        val mList: DragDropSwipeRecyclerView = binding.list1
        mList.layoutManager = LinearLayoutManager(context)
        mList.adapter = tareasCompletadasAdapter

        mList.orientation =
            DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING
        mList.reduceItemAlphaOnSwiping = false
        mList.disableSwipeDirection(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.RIGHT)

        val onItemSwipeListener = object : OnItemSwipeListener<TareaEntity> {
            override fun onItemSwiped(
                position: Int,
                direction: OnItemSwipeListener.SwipeDirection,
                item: TareaEntity
            ): Boolean {
                when (direction) {
                    //Completar Tarea
                    OnItemSwipeListener.SwipeDirection.RIGHT_TO_LEFT -> {
                        tareaViewModel.deleteTarea(item)
                    }
                    else -> return false
                }
                return false
            }
        }
        mList.swipeListener = onItemSwipeListener
    }

    private fun fabAddItem() {
        binding.fab.setOnClickListener {
            Log.d("Main", "Button pressed")
            val builder = AlertDialog.Builder(context!!)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.et_editText)

            with(builder) {
                setTitle("Crea una tarea!")
                setPositiveButton("ACEPTAR") { dialog, which ->
                    tareaViewModel.insertTarea(
                        TareaEntity(
                            tarea = editText.text.toString(),
                            estado = "Pendiente"
                        )
                    )

                    FancyToast.makeText(
                        activity!!.applicationContext,
                        "Tarea añadida correctamente",
                        Toast.LENGTH_SHORT,
                        FancyToast.SUCCESS,
                        false
                    ).show()
                }
                setNegativeButton("CANCELAR") { dialog, which ->
                }
                setView(dialogLayout)
                show()
            }
        }
    }

    //Configuración de toolbar
    private fun toolbarConfig() {
        binding.toolbar.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, activity?.getStatusBarHeight()!!.plus(10), 0, 0)
        }

        binding.toolbar.toolbar.title = "Mis Tareas"
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar)

        (requireActivity() as AppCompatActivity).apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.header_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        findNavController().navigateUp()
                        true
                    }

                    R.id.action_notification -> {
                        findNavController().navigate(R.id.global_notificationFragment)
                        return true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}


