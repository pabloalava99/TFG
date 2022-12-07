package com.viamedsalud.gvp.ui.episodio

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shashank.sony.fancytoastlib.FancyToast
import com.viamedsalud.gvp.R
import com.viamedsalud.gvp.api.response.EpisodioResponse
import com.viamedsalud.gvp.databinding.FragmentTrabajoListBinding
import com.viamedsalud.gvp.model.Episodio
import com.viamedsalud.gvp.model.HClinica
import com.viamedsalud.gvp.model.Trabajo
import com.viamedsalud.gvp.ui.episodio.adapter.HClinicaListAdapter
import com.viamedsalud.gvp.ui.episodio.adapter.TrabajoListAdapter
import com.viamedsalud.gvp.ui.episodio.viewmodel.EpisodioViewModel
import com.viamedsalud.gvp.ui.episodio.viewmodel.HClinicaViewModel
import com.viamedsalud.gvp.util.getStatusBarHeight
import com.viamedsalud.gvp.utils.BaseResponse
import com.viamedsalud.gvp.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class TrabajoListFragment : Fragment() {

    lateinit var binding: FragmentTrabajoListBinding

    lateinit var trabajoListAdapter: TrabajoListAdapter

    private val hclinicaViewModel by viewModels<HClinicaViewModel>()
    private val episodioViewModel by viewModels<EpisodioViewModel>()


    private var trabajoMutableList: MutableList<Trabajo> = emptyList<Trabajo>().toMutableList()

    @Inject
    lateinit var sessionManager: SessionManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_trabajo_list, container, false)

        toolbarConfig()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idUser: Int? = sessionManager.getId()
        hclinicaViewModel.getHClinicasUser(idUser!!)


        //Adaptador del historial clinico
        binding.recyclerviewTrabajo.apply {
            trabajoListAdapter = TrabajoListAdapter()
            adapter = trabajoListAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        observadorHClinca()


        filterList()
    }

    //Filtro para la lista
    private fun filterList() {
        binding.filtro.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val trabajoFiltered =
                    trabajoMutableList.filter { trabajo ->
                        trabajo.episodio!!.lowercase()
                            .contains(newText.toString().lowercase()) ||
                        trabajo.cama!!.lowercase()
                            .contains(newText.toString().lowercase()) ||
                        trabajo.nom_paciente!!.lowercase()
                            .contains(newText.toString().lowercase()) ||
                        trabajo.apell_paciente!!.lowercase()
                            .contains(newText.toString().lowercase()) ||
                        trabajo.evolucion!!.lowercase()
                            .contains(newText.toString().lowercase())
                    }
                trabajoListAdapter.updateTrabajo(trabajoFiltered)
                return false
            }
        })
    }

    //Observador mis Tareas
    private fun observadorHClinca() {
        hclinicaViewModel.hclinicasLiveDataUser.observe(viewLifecycleOwner, Observer {
            val formatoDiaHora =
                SimpleDateFormat("dd/MM/yyyy HH:mm:ss")//Formato de dia y hora
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

            when (it) {
                is BaseResponse.Success -> {

                    var listaHClinica: List<HClinica> = it.data!!

                    for (i in listaHClinica.indices) {

                        //Obtenemos el historial clinico del usuario
                        listaHClinica[i].fecha =
                            formatoDiaHora.format(parser.parse(listaHClinica[i].fecha!!))

                        episodioViewModel.getEpisodio(listaHClinica[i].episodio!!) //Obtenemos le episodio
                        //Observador del episodio de la historia clinica
                        episodioViewModel.episodioResponseLiveData.observe(
                            viewLifecycleOwner,
                            Observer {
                                when (it) {
                                    is BaseResponse.Success -> {
                                        stopLoading()
                                        var episodio: EpisodioResponse? = it.data
                                        trabajoMutableList.add(
                                            Trabajo(
                                                listaHClinica[i].id,
                                                listaHClinica[i].fecha,
                                                listaHClinica[i].evolucion,
                                                listaHClinica[i].episodio,
                                                episodio!!.nomPaciente,
                                                episodio!!.apellPaciente,
                                                episodio!!.cama
                                            )
                                        )

                                        trabajoListAdapter.trabajo = trabajoMutableList

                                    }
                                    is BaseResponse.Error -> {
                                        stopLoading()
                                        processError(it.msg)
                                    }

                                }
                            })

                    }
                }
                is BaseResponse.Error -> {
                    stopLoading()
                    processError(it.msg)
                }
                is BaseResponse.Loading -> {
                    showLoading()
                }

            }
        })
    }


    //Proceso de Error
    private fun processError(msg: String?) {
        FancyToast.makeText(
            activity!!.applicationContext,
            "Error:" + msg,
            Toast.LENGTH_SHORT,
            FancyToast.INFO,
            false
        ).show()
    }

    //Mostrar Loading
    fun showLoading() {
        binding.trabajosShimmer.visibility = View.VISIBLE
        binding.recyclerviewTrabajo.visibility = View.INVISIBLE
    }

    //Ocultar Loading
    fun stopLoading() {
        binding.recyclerviewTrabajo.visibility = View.VISIBLE
        binding.trabajosShimmer.visibility = View.INVISIBLE

    }

    //Configuración de toolbar
    private fun toolbarConfig() {
        binding.toolbar.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, activity?.getStatusBarHeight()!!.plus(10), 0, 0)
        }

        binding.toolbar.toolbar.title = "Mis Trabajos"
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


