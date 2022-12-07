package com.viamedsalud.gvp.ui.episodio

import android.os.Bundle
import android.view.*
import android.widget.SearchView
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.shashank.sony.fancytoastlib.FancyToast
import com.viamedsalud.gvp.R
import com.viamedsalud.gvp.databinding.FragmentEpisodioListBinding
import com.viamedsalud.gvp.handler.EpisodioHandler
import com.viamedsalud.gvp.model.Episodio
import com.viamedsalud.gvp.ui.episodio.adapter.EpisodioListAdapter
import com.viamedsalud.gvp.ui.episodio.viewmodel.EpisodioViewModel
import com.viamedsalud.gvp.util.getStatusBarHeight
import com.viamedsalud.gvp.utils.BaseResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class EpisodioListFragment : Fragment(), EpisodioHandler {

    lateinit var binding: FragmentEpisodioListBinding

    lateinit var episodioListAdapter: EpisodioListAdapter

    private val episodioviewModel by viewModels<EpisodioViewModel>()

    private var episodiosMutableList: MutableList<Episodio> = emptyList<Episodio>().toMutableList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_episodio_list, container, false)

        toolbarConfig()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        episodioviewModel.getEpisodios()
        bindObservers()
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
                val pacientesFiltered =
                    episodiosMutableList.filter { episodio ->
                        episodio.nom_paciente!!.lowercase()
                            .contains(newText.toString().lowercase()) ||
                                episodio.apell_paciente!!.lowercase()
                                    .contains(newText.toString().lowercase()) ||
                                episodio.cama!!.lowercase().contains(newText.toString().lowercase())
                    }
                episodioListAdapter.updatePacientes(pacientesFiltered)
                return false
            }
        })
    }

    //Observador
    private fun bindObservers() {
        episodioviewModel.episodiosLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BaseResponse.Success -> {
                    stopLoading()
                    episodioListAdapter = EpisodioListAdapter(this)
                    binding.recyclerviewEpisodio.apply {
                        adapter = episodioListAdapter
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    }
                    episodioListAdapter.episodios = it.data!!
                    episodiosMutableList = it.data.toMutableList()
                }
                is BaseResponse.Error -> {
                    stopLoading()
                    processError(it.msg)
                }
                is BaseResponse.Loading -> {
                    showLoading()
                }else -> {
                stopLoading()
                }
            }
        })
    }

    //Click en episodio de la lista
    override fun onEpisodioClicked(episodio: String) {
        //Mandamos el episodio a Fragmento Paciente
        setFragmentResult("solicitud", bundleOf("data" to episodio))
        findNavController().navigate(R.id.action_episodioListFragment_to_episodioFragment2)
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

    //Configuración de toolbar
    private fun toolbarConfig() {
        binding.toolbar.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, activity?.getStatusBarHeight()!!.plus(10), 0, 0)
        }

        binding.toolbar.toolbar.title = "Pacientes"
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

    //Mostrar Loading
    fun showLoading() {
        binding.episodiosShimmer.visibility = View.VISIBLE
        binding.recyclerviewEpisodio.visibility = View.INVISIBLE
    }

    //Ocultar Loading
    fun stopLoading() {
        binding.recyclerviewEpisodio.visibility = View.VISIBLE
        binding.episodiosShimmer.visibility = View.INVISIBLE

    }
}


