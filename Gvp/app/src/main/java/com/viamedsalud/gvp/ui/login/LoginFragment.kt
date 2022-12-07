package com.viamedsalud.gvp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.orhanobut.logger.Logger
import com.viamedsalud.gvp.utils.SessionManager
import com.shashank.sony.fancytoastlib.FancyToast
import com.viamedsalud.gvp.R
import com.viamedsalud.gvp.api.request.UserRequest
import com.viamedsalud.gvp.utils.BaseResponse
import com.viamedsalud.gvp.api.response.UserResponse
import com.viamedsalud.gvp.databinding.FragmentLoginBinding
import com.viamedsalud.gvp.ui.dashboard.DashboardActivity
import com.viamedsalud.gvp.ui.login.viewmodel.LoginViewModel
import com.viamedsalud.gvp.util.Helper
import com.viamedsalud.gvp.util.getStatusBarHeight
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding

    private val loginViewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_login, container, false)
        binding.handler = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutForm.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, activity?.getStatusBarHeight()!!.plus(10), 0, 0)
        }
        val token = sessionManager.getToken()

        if (!token.isNullOrBlank()) {
            activity?.finishAffinity()
            startActivity(Intent(context, DashboardActivity::class.java))
        }

        bindObservers()
    }

    //Funcion click en el botón Iniciar Sesion
    fun onLoginClicked() {
        val validationResult = validateUserInput()
        if (validationResult.first) {
            val userRequest = getUserRequest()
            loginViewModel.loginUser(userRequest)
        } else {
            processInfo(validationResult.second)
        }
        Helper.hideKeyboard(view!!)
    }

    //Validamos el Input del Usuario
    private fun validateUserInput(): Pair<Boolean, String> {
        val userName = binding.name.text.toString()
        val password = binding.password.text.toString()
        return loginViewModel.validateCredentials(userName, password, true)
    }

    //Obtenemos el input
    private fun getUserRequest(): UserRequest {
        return binding.run {
            UserRequest(
                name.text.toString(),
                password.text.toString()
            )
        }
    }

    //Observador
    private fun bindObservers() {
        loginViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BaseResponse.Success -> {
                    stopLoading()
                    processLogin(it.data)
                }
                is BaseResponse.Error -> {
                    stopLoading()
                    Logger.e("hello");
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

    //Proceso de Login
    private fun processLogin(data: UserResponse?) {
        if (!data?.token.isNullOrEmpty()) {
            sessionManager.saveToken(data!!.token)
            sessionManager.saveId(data!!.id)
            activity?.finishAffinity()
            startActivity(Intent(context, DashboardActivity::class.java))
        }
    }

    //Proceso de Error
    private fun processError(msg: String?) {
        FancyToast.makeText(
            activity!!.applicationContext,
            msg,
            Toast.LENGTH_SHORT,
            FancyToast.ERROR,
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

    fun showLoading() {
        binding.loader.visibility = View.VISIBLE
    }
    fun stopLoading() {
        binding.loader.visibility = View.GONE
    }


}