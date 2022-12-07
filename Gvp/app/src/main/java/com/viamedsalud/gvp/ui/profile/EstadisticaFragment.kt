package com.viamedsalud.gvp.ui.profile

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.shashank.sony.fancytoastlib.FancyToast
import com.viamedsalud.gvp.R
import com.viamedsalud.gvp.databinding.FragmentEstadisiticaBinding
import com.viamedsalud.gvp.model.HClinica
import com.viamedsalud.gvp.ui.episodio.viewmodel.HClinicaViewModel
import com.viamedsalud.gvp.util.getStatusBarHeight
import com.viamedsalud.gvp.utils.BaseResponse
import com.viamedsalud.gvp.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EstadisticaFragment : Fragment() {
    lateinit var binding: FragmentEstadisiticaBinding

    private val hclinicaViewModel by viewModels<HClinicaViewModel>()

    @Inject
    lateinit var sessionManager: SessionManager

    var nVisitaRealizada :Float = 0F
    var nMedicacionSubministrada :Int = 0
    var nConstantesTomadas :Int = 0
    var nTemperaturaTomada :Int = 0
    var nOtro :Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_estadisitica, container, false)

        toolbarConfig()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idUser: Int? = sessionManager.getId()
        hclinicaViewModel.getHClinicasUser(idUser!!)

        observadorEstadisitica()
    }


    fun makePieChart() {
        setupPieChart()
        loadPieChartData()
    }

    private fun observadorEstadisitica() {
        hclinicaViewModel.hclinicasLiveDataUser.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BaseResponse.Success -> {

                    var listaHClinica: List<HClinica> = it.data!!

                    for (i in listaHClinica.indices) {
                        when(listaHClinica[i].evolucion){
                            "Visita Realizada" -> nVisitaRealizada++
                            "Medicación subministrada" -> nMedicacionSubministrada++
                            "Constantes tomadas" -> nConstantesTomadas++
                            "Temperatura tomada" -> nTemperaturaTomada++
                            else -> nOtro++
                        }
                    }

                    makePieChart()
                    binding.txtVisitaRealizada.text = nVisitaRealizada.toInt().toString()
                    binding.txtConstantesTomadas.text = nConstantesTomadas.toInt().toString()
                    binding.txtMedicacionSubministrada.text = nMedicacionSubministrada.toInt().toString()
                    binding.txtTemperaturaTomada.text = nMedicacionSubministrada.toInt().toString()
                    binding.txtOtro.text = nOtro.toInt().toString()

                }
                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                is BaseResponse.Loading -> {

                }
            }
        })
    }

    private fun setupPieChart() {
        binding.grafica.isDrawHoleEnabled = true
        binding.grafica.setUsePercentValues(false)
        binding.grafica.setEntryLabelTextSize(12f)
        binding.grafica.setEntryLabelColor(Color.WHITE)
        binding.grafica.description.isEnabled = false
        val l: Legend = binding.grafica.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.isEnabled = false
    }

    private fun loadPieChartData() {
        val entries: ArrayList<PieEntry> = ArrayList()

        entries.add(PieEntry(nVisitaRealizada, "Visita Realizada"))
        entries.add(PieEntry(nMedicacionSubministrada.toFloat(), "Medicación subministrada"))
        entries.add(PieEntry(nConstantesTomadas.toFloat(), "Constantes tomadas"))
        entries.add(PieEntry(nTemperaturaTomada.toFloat(), "Temperatura tomada"))
        entries.add(PieEntry(nOtro.toFloat(), "Otro"))
        val colors: ArrayList<Int> = ArrayList()

        colors.add(ContextCompat.getColor(requireContext(),R.color.colorSecondary))
        colors.add(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
        colors.add(ContextCompat.getColor(requireContext(),R.color.colorVariant3))
        colors.add(ContextCompat.getColor(requireContext(),R.color.colorVariant4))
        colors.add(ContextCompat.getColor(requireContext(),R.color.colorVariant5))

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        val data = PieData(dataSet)
        data.setDrawValues(true)

        data.setValueFormatter(FormatoGrafica())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.SERIF)
        data.setValueTextColor(Color.WHITE)
        binding.grafica.data = data
        binding.grafica.invalidate()
        binding.grafica.animateY(3000, Easing.EaseInOutQuart)
    }

    private fun toolbarConfig() {
        binding.toolbar.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, activity?.getStatusBarHeight()!!.plus(10), 0, 0)
        }

        binding.toolbar.toolbar.title = "Estadisticas"
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


}

private class FormatoGrafica : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.toInt().toString()
    }
}