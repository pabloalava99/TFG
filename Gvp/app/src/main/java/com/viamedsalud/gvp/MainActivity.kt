package com.viamedsalud.gvp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.viamedsalud.gvp.databinding.ActivityMainBinding
import com.viamedsalud.gvp.ui.login.FormActivity
import com.viamedsalud.gvp.util.Statusbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Statusbar.setStatusbarTheme(this, window, 0, binding.root)
    }

    private fun changeActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(applicationContext, FormActivity::class.java))
            finish()
        }, 500)
    }

    override fun onResume() {
        super.onResume()
        changeActivity()
    }

}