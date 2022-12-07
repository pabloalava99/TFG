package com.viamedsalud.gvp.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.zxing.integration.android.IntentIntegrator
import com.shashank.sony.fancytoastlib.FancyToast
import com.viamedsalud.gvp.R
import com.viamedsalud.gvp.utils.BaseResponse
import com.viamedsalud.gvp.databinding.FragmentHomeBinding
import com.viamedsalud.gvp.model.HClinica
import com.viamedsalud.gvp.ui.episodio.viewmodel.HClinicaViewModel
import com.viamedsalud.gvp.ui.login.viewmodel.LoginViewModel
import com.viamedsalud.gvp.util.Statusbar
import com.viamedsalud.gvp.util.getStatusBarHeight
import com.viamedsalud.gvp.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    private val loginViewModel by viewModels<LoginViewModel>()

    private val hClinicaViewModel by viewModels<HClinicaViewModel>()


    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.handler = this
        Statusbar.setStatusbarTheme(
            requireContext(),
            requireActivity().window,
            R.color.lightGrey,
            binding.root
        )

        //Llamada al toolbar
        toolbarConfig()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressView.setProgress(4, true)



        var idUser: Int? = sessionManager.getId()

        if (idUser != null) {
            loginViewModel.getUser(idUser)
            hClinicaViewModel.getHClinicasUser(idUser)
        }

        obtenerVisitasHoy()
        bindObservers()
    }

    //Observador
    private fun bindObservers() {
        loginViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BaseResponse.Success -> {
                    binding.nombrePersonal.text = it.data?.username ?: ""
                }
                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                is BaseResponse.Loading -> {
                }
                else -> {
                }
            }
        })
    }

    private fun obtenerVisitasHoy() {
        hClinicaViewModel.hclinicasLiveDataUser.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BaseResponse.Success -> {
                    val formatoDia = SimpleDateFormat("dd")//Formato dia
                    val diaHoy = formatoDia.format(Date())//Obtener dia de hoy

                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

                    var listaVisitasHoy: List<HClinica> = it.data!!
                    var visitasHoy = 0

                    for (i in listaVisitasHoy.indices) {
                        if(formatoDia.format(parser.parse(listaVisitasHoy[i].fecha!!)) == diaHoy){
                            when(listaVisitasHoy[i].evolucion){
                                "Visita Realizada" -> visitasHoy++
                            }
                        }
                    }
                    //Imprimimos en el progressView las visitas que se han realizado hoy
                    binding.progressView.setProgress(visitasHoy, true)
                }
                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                is BaseResponse.Loading -> {
                }
                else -> {
                }
            }
        })
    }

    fun processError(msg: String?) {
        FancyToast.makeText(
            activity!!.applicationContext,
            "Error:" + msg,
            Toast.LENGTH_SHORT,
            FancyToast.INFO,
            false
        ).show()
    }

    private fun toolbarConfig() {
        binding.toolbar.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, activity?.getStatusBarHeight()!!.plus(10), 0, 0)
        }

        binding.layoutHeader.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, activity?.getStatusBarHeight()!!.plus(10), 0, 0)
        }

        binding.toolbar.toolbar.title = ""
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar)

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


    fun pacienteClicked() {
        initScanner()
    }

    fun onListPacientesClicked() {
        findNavController().navigate(R.id.action_homeFragment_to_episodioListFragment)
    }

    fun onHistoryClicked() {
        findNavController().navigate(R.id.action_homeFragment_to_HClinicaListFragment)
    }

    fun onTareasClicked() {
        findNavController().navigate(R.id.action_homeFragment_to_camaListFragment)
    }

    fun onReportClicked() {
        findNavController().navigate(R.id.action_homeFragment_to_reportFragment2)
    }


    //Funcion para iniciar el Scanner
    private fun initScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.CODE_39)
        integrator.setPrompt("ESCANEA LA PULSERA")
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    //Funcion para obtener el resultad del Scanner
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                findNavController().navigate(R.id.homeFragment)
            } else {
                findNavController().navigate(R.id.episodioFragment2)
                //Mandamos los datos del escaner
                setFragmentResult("solicitud", bundleOf("data" to result.contents))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}