package com.arfdevs.productmonitoring.presentation.view.ui.login

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.navigation.fragment.findNavController
import com.arfdevs.productmonitoring.R
import com.arfdevs.productmonitoring.databinding.FragmentLoginBinding
import com.arfdevs.productmonitoring.helper.Constants
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.helper.enabled
import com.arfdevs.productmonitoring.helper.visible
import com.arfdevs.productmonitoring.presentation.view.base.BaseFragment
import com.arfdevs.productmonitoring.presentation.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
) {

    private val authVM: AuthViewModel by activityViewModel()

    override fun initView() = with(binding) {
        tvLoginTitle.text = getString(R.string.tv_sign_in)
        tiUsername.hint = getString(R.string.ti_username_hint)
        tiPassword.hint = getString(R.string.ti_password_hint)
        btnLogin.text = getString(R.string.btn_login)

        usernameValidation()
        passwordValidation()

        initListener()
        initObserver()
    }


    private fun initListener() = with(binding) {
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            authVM.login(
                username,
                password
            )
        }
    }

    private fun initObserver() = with(authVM) {
        session.observe(viewLifecycleOwner) { state ->
            when (state) {
                UiState.Loading -> {}
                is UiState.Success -> {
                    showError(false)
                    findNavController().navigate(R.id.action_loginFragment_to_attendanceFragment)
                }

                UiState.Empty -> showError(true)
                is UiState.Error -> showError(true, state.message)
                UiState.ErrorConnection -> showError(true, "Error Connection")
            }
        }
    }

    private fun showError(state: Boolean, message: String = "") = with(binding) {
        errorView.visible(state)
        val msg = message.ifEmpty { "Empty" }
        errorView.setMessage(
            "Login failed",
            msg
        ) {
            errorView.visible(false)
        }
    }

    private fun usernameValidation() = with(binding) {
        etUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    validateFields()
                }
            }

        })
    }

    private fun passwordValidation() = with(binding) {
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    validateFields()
                }
            }

        })
    }

    private fun validateFields() = with(binding) {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        val isUsernameBlank = username.isBlank()
        val isUsernameValid =
            Patterns.EMAIL_ADDRESS.matcher(username).matches() && username.isNotBlank()

        val isPasswordBlank = password.isBlank()
        val isPasswordValid =
            password.length >= Constants.PASSWORD_MAX_LENGTH && password.isNotBlank()

        when {
            isUsernameBlank -> {
                tiUsername.apply {
                    isHelperTextEnabled = false
                    error = context.getString(R.string.err_username_empty)
                    isErrorEnabled = true
                }
                btnLogin.enabled(false)
            }

            !isUsernameValid -> {
                tiUsername.apply {
                    isHelperTextEnabled = false
                    error = context.getString(R.string.err_username_invalid)
                    isErrorEnabled = true
                }
                btnLogin.enabled(false)
            }

            isPasswordBlank -> {
                tiPassword.apply {
                    isHelperTextEnabled = false
                    error = context.getString(R.string.err_password_empty)
                    isErrorEnabled = true
                }
                btnLogin.enabled(false)
            }

            !isPasswordValid -> {
                tiPassword.apply {
                    isHelperTextEnabled = false
                    error = context.getString(R.string.err_password_invalid)
                    isErrorEnabled = true
                }
                btnLogin.enabled(false)
            }

            else -> {
                tiUsername.isErrorEnabled = false
                tiPassword.isErrorEnabled = false
                btnLogin.enabled(true)
            }
        }
    }

}
