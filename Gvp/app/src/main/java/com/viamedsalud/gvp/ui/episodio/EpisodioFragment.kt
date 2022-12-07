package com.viamedsalud.gvp.ui.episodio

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import com.shashank.sony.fancytoastlib.FancyToast
import com.viamedsalud.gvp.R
import com.viamedsalud.gvp.api.request.HClinicaRequest
import com.viamedsalud.gvp.api.response.EpisodioResponse
import com.viamedsalud.gvp.databinding.FragmentEpisodioBinding
import com.viamedsalud.gvp.model.HClinica
import com.viamedsalud.gvp.ui.episodio.adapter.HClinicaListAdapter
import com.viamedsalud.gvp.ui.episodio.adapter.HClinicaListHoyAdapter
import com.viamedsalud.gvp.ui.episodio.viewmodel.EpisodioViewModel
import com.viamedsalud.gvp.ui.episodio.viewmodel.HClinicaViewModel
import com.viamedsalud.gvp.ui.login.viewmodel.LoginViewModel
import com.viamedsalud.gvp.util.Helper
import com.viamedsalud.gvp.util.getStatusBarHeight
import com.viamedsalud.gvp.utils.BaseResponse
import com.viamedsalud.gvp.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class EpisodioFragment : Fragment() {

    lateinit var binding: FragmentEpisodioBinding

    //ViewModels
    private val episodioViewModel by viewModels<EpisodioViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()
    private val hClinicaViewModel by viewModels<HClinicaViewModel>()
    private val
            hClinicaViewModelVisitasHoy by viewModels<HClinicaViewModel>()
    private val
            hClinicaViewModelEspisodios by viewModels<HClinicaViewModel>()


    //Adaptadores
    lateinit var hClinicaListHoyAdapter: HClinicaListHoyAdapter
    lateinit var hClinicaListAdapter: HClinicaListAdapter

    //Listas mutable
    private var hclinicaMutableListHoy: MutableList<HClinica> =
        emptyList<HClinica>().toMutableList()
    private var hclinicaMutableList: MutableList<HClinica> = emptyList<HClinica>().toMutableList()


    @Inject
    lateinit var sessionManager: SessionManager

    //Variables
    private var episodio: String = ""
    private var userName: String = ""
    var idUser: Int? = -1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_episodio, container, false)
        binding.handler = this

        toolbarConfig()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Obtenemos el episodio
        setFragmentResultListener("solicitud") { _, bundle ->
            episodio = bundle.getString("data").toString()
            episodioViewModel.getEpisodio(episodio)
            Logger.i(episodio)
            hClinicaViewModelEspisodios.getHClinicasEpisodio(episodio) //Obtenemos las HClinicas por episodio
        }
        idUser = sessionManager.getId() //Obtenemos el id Usuario
        hClinicaViewModelVisitasHoy.getAllHClinicas() //Obtenemos todas las HClinicas

        observeEpisodio()
        observeUsuario()
        observeMakeHclinica()
        observeVisitasHoy()
        observeHclinicaEpisodio()
        rellenarOpciones()

    }

    private fun toolbarConfig() {
        binding.toolbar.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, activity?.getStatusBarHeight()!!.plus(10), 0, 0)
        }

        binding.toolbar.toolbar.title = ""
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

    //Observadores
    private fun observeEpisodio() {

        //Observador Recuperar Episodio
        episodioViewModel.episodioResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BaseResponse.Success -> {
                    stopLoading()
                    showData(it.data!!)
                }
                is BaseResponse.Error -> {
                    stopLoading()
                    processError(it.msg)
                }
                is BaseResponse.Loading -> {
                    showLoading()
                }
                else -> {
                    stopLoading()
                }
            }
        })
    }

    private fun observeUsuario() {

        //Observador Recuperar Usuario
        loginViewModel.getUser(idUser!!)
        loginViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BaseResponse.Success -> {
                    userName = it.data!!.username
                }
                is BaseResponse.Error -> {
                    processError(it.msg)
                }
            }
        })
    }

    private fun observeMakeHclinica() {

        //Observador Crear HClinica
        hClinicaViewModel.hclinicaResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BaseResponse.Success -> {
                    stopLoading()
                    processSuccess(it.data!!.message)
                }
                is BaseResponse.Error -> {
                    stopLoading()
                    processError(it.msg)
                }
                is BaseResponse.Loading -> {
                    showLoading()
                }
                else -> {
                    stopLoading()
                }
            }
        })
    }

    private fun observeVisitasHoy() {

        //Observador recuperar las Visitas de hoy
        hClinicaViewModelVisitasHoy.hclinicasLiveDataHoy.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BaseResponse.Success -> {
                    val formatoDia = SimpleDateFormat("dd")//Formato dia
                    val diaHoy = formatoDia.format(Date())//Obtener dia de hoy
                    val formatoTiempo = SimpleDateFormat("HH:mm")//Formato de tiempo

                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

                    var hclinicaListaHoy: List<HClinica> = it.data!!
                    hclinicaMutableListHoy =
                        emptyList<HClinica>().toMutableList() //Vaciamos la lista

                    for (i in hclinicaListaHoy.indices) {

                        //Obtenemos el historial clinico de hoy
                        if (formatoDia.format(parser.parse(hclinicaListaHoy[i].fecha!!)) == diaHoy &&
                            hclinicaListaHoy[i].episodio == episodio
                        ) {
                            hclinicaListaHoy[i].fecha =
                                formatoTiempo.format(parser.parse(hclinicaListaHoy[i].fecha!!))
                            hclinicaMutableListHoy.add(hclinicaListaHoy[i])
                        }

                    }
                    //Adaptador de historial clinico Hoy
                    binding.recyclerviewVisitasHoy.apply {
                        hClinicaListHoyAdapter = HClinicaListHoyAdapter()
                        adapter = hClinicaListHoyAdapter
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    }
                    hClinicaListHoyAdapter.hclinica = hclinicaMutableListHoy
                }
                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                is BaseResponse.Loading -> {

                }
            }
        })

    }

    private fun observeHclinicaEpisodio() {

        //Observador recuperar las Visitas de Episodio
        hClinicaViewModelEspisodios.hclinicasLiveDataEpisodio.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BaseResponse.Success -> {
                    val formatoDiaHora =
                        SimpleDateFormat("dd/MM/yyyy HH:mm:ss")//Formato de dia y hora
                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

                    var hclinicaListaEpisodio: List<HClinica> = it.data!!
                    hclinicaMutableList = emptyList<HClinica>().toMutableList() //Vaciamos la lista

                    for (i in hclinicaListaEpisodio.indices) {
                        //Obtenemos el historial clinico del episodio
                        hclinicaListaEpisodio[i].fecha =
                            formatoDiaHora.format(parser.parse(hclinicaListaEpisodio[i].fecha!!))
                        hclinicaMutableList.add(hclinicaListaEpisodio[i])
                    }

                    //Adaptador del historial clinico
                    binding.recyclerviewHistorialClinico.apply {
                        hClinicaListAdapter = HClinicaListAdapter()
                        adapter = hClinicaListAdapter
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    }
                    hClinicaListAdapter.hclinica = hclinicaMutableList

                }
                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                is BaseResponse.Loading -> {

                }
            }
        })

    }

    //Rellenamos las opciones de Crear Evolución
    fun rellenarOpciones() {
        var arrayOpciones = arrayOf(
            "Visita Realizada",
            "Medicación subministrada",
            "Constantes tomadas",
            "Temperatura tomada"
        )

        val adapter = ArrayAdapter(context!!, R.layout.row_opciones, arrayOpciones)

        binding.expandableListview.adapter = adapter
        binding.expandableListview.isExpanded = true
    }

    //Imprimir los datos
    fun showData(episodio: EpisodioResponse) {
        val formatter = SimpleDateFormat("dd/MM/yyyy")//Formato de fecha

        binding.nombre.text = episodio.nomPaciente + " " + episodio.apellPaciente
        binding.cama.text = episodio.cama
        binding.sexo.text = when (episodio.sexo) {
            "H" -> "Hombre"
            "M" -> "Mujer"
            else -> {
                "No especificado"
            }
        }
        binding.fechaNac.text = formatter.format(episodio.fechaNac).toString()
        binding.diagnostico.text = episodio.diagnostico
        binding.alergias.text = episodio.alergia
        binding.medicacion.text = episodio.medicacion
        if (episodio.alergia != "") {
            binding.alergiasIcono.visibility = View.VISIBLE
        }
        if (Helper.obtenerEdad(episodio.fechaNac) < 18) {
            binding.ninoIcono.visibility = View.VISIBLE
        }
    }

    //Boton Evolución
    fun onEvolucionClicked() {
        binding.expandableListview.visibility = View.VISIBLE
        binding.btnCrearEvolucion.visibility = View.VISIBLE
        binding.btnEvolucion.visibility = View.GONE
        binding.Opc5.visibility = View.VISIBLE

        binding.Opc5.setOnClickListener {
            binding.Opc5.isChecked = !binding.Opc5.isChecked
            binding.Opc5txt.visibility =
                if (binding.Opc5.isChecked) View.VISIBLE else View.GONE
            Helper.hideKeyboard(view!!)
        }


    }

    //Botón Crear Evolución
    fun onCrearEvolucionClicked() {

        for (i in 0 until binding.expandableListview.count) {
            if (binding.expandableListview.isItemChecked(i)) {
                crearEvolucion(
                    idUser!!,
                    userName,
                    episodio,
                    binding.expandableListview.getItemAtPosition(i).toString()
                )
                binding.expandableListview.setItemChecked(i, false)
            }
        }
        if (binding.Opc5.isChecked && !binding.Opc5txt.text.isEmpty()) {
            crearEvolucion(idUser!!, userName, episodio, binding.Opc5txt.text.toString())
        } else if (binding.Opc5txt.text.isEmpty() && binding.Opc5.isChecked) {
            processInfo("El campo 'Otro' esta vacio")
        }

        hClinicaViewModel.getAllHClinicas()

        binding.Opc5.isChecked = false
        binding.Opc5.visibility = View.GONE
        binding.Opc5txt.visibility = View.GONE
        binding.expandableListview.visibility = View.GONE
        binding.btnCrearEvolucion.visibility = View.GONE
        binding.btnEvolucion.visibility = View.VISIBLE
    }

    //Crear Evolución
    fun crearEvolucion(idUser: Int, nameUser: String, evolucion: String, episodio: String) {

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val fechaActual = sdf.format(Date())

        val hClinicaRequest = HClinicaRequest(fechaActual, idUser, nameUser, episodio, evolucion)
        hClinicaViewModel.createHClinica(hClinicaRequest)

    }

    //Proceso Error
    fun processError(msg: String?) {
        findNavController().navigate(R.id.homeFragment)
        FancyToast.makeText(
            activity!!.applicationContext,
            msg,
            Toast.LENGTH_SHORT,
            FancyToast.ERROR,
            false
        ).show()
    }

    //Proceso Correcto
    fun processSuccess(msg: String?) {
        FancyToast.makeText(
            activity!!.applicationContext,
            msg,
            Toast.LENGTH_SHORT,
            FancyToast.SUCCESS,
            false
        ).show()
    }

    //Proceso de Informacion
    private fun processInfo(msg: String?) {
        FancyToast.makeText(
            activity!!.applicationContext,
            msg,
            Toast.LENGTH_SHORT,
            FancyToast.INFO,
            false
        ).show()
    }

    //Mostrar Loading
    fun showLoading() {
        binding.generalShimmer.visibility = View.VISIBLE
        binding.general.visibility = View.INVISIBLE
        binding.diagnosticoShimmer.visibility = View.VISIBLE
        binding.alergiasShimmer.visibility = View.VISIBLE
        binding.alergiasShimmer.visibility = View.VISIBLE

    }

    //Ocultar Loading
    fun stopLoading() {
        binding.general.visibility = View.VISIBLE
        binding.generalShimmer.visibility = View.INVISIBLE

        binding.diagnosticoShimmer.visibility = View.GONE
        binding.medicacionShimmer.visibility = View.GONE
        binding.alergiasShimmer.visibility = View.GONE

        binding.diagnostico.visibility = View.VISIBLE
        binding.medicacion.visibility = View.VISIBLE
        binding.alergias.visibility = View.VISIBLE
    }


}