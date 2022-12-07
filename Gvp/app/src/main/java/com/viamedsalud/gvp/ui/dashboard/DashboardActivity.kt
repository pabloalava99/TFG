package com.viamedsalud.gvp.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.viamedsalud.gvp.R
import com.viamedsalud.gvp.databinding.ActivityDashboardBinding
import com.viamedsalud.gvp.util.Statusbar
import com.viamedsalud.gvp.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    lateinit var binding: ActivityDashboardBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        Statusbar.setStatusbarTheme(this, window, 0, binding.root)

       makeDashboard()
    }

    private fun makeDashboard() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.dashboard_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val popupMenu = PopupMenu(this, binding.root)
        popupMenu.inflate(R.menu.menu_dashboard)
        binding.navigationDashboard.setupWithNavController(popupMenu.menu, navController)

        navController.addOnDestinationChangedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

    //Cambia el destino de la Navegación
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.homeFragment,
            R.id.editProfileFragment -> {
                binding.navigationDashboard.visibility = View.VISIBLE
            }
            R.id.tareaListFragment -> {
                binding.navigationDashboard.visibility = View.VISIBLE
            }
            else -> {
                hideBottomBav()
            }
        }
    }

    private fun hideBottomBav() {
        binding.navigationDashboard.visibility = View.GONE
    }

    fun statusbarIconDark(boolean: Boolean) {
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
            boolean
    }


}