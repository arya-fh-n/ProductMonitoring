package com.arfdevs.productmonitoring.presentation.view.ui.login

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arfdevs.productmonitoring.R
import com.arfdevs.productmonitoring.databinding.FragmentLoginBinding
import com.arfdevs.productmonitoring.helper.Constants
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.helper.visible
import com.arfdevs.productmonitoring.presentation.view.base.BaseFragment
import com.arfdevs.productmonitoring.presentation.viewmodel.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

        setupFieldValidators()
        initListener()
        initObserver()
    }


    private fun initListener() = with(binding) {
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            hideKeyboard()
            authVM.login(username, password)
        }
    }

    private fun initObserver() = with(authVM) {
        session.observe(viewLifecycleOwner) { state ->
            showLoading(state is UiState.Loading)
            when (state) {
                is UiState.Success -> {
                    showLoginStatus(getString(R.string.login_success)) {
                        findNavController().navigate(R.id.action_loginFragment_to_dashboardActivity)
                    }
                }

                UiState.Empty -> showLoginStatus(getString(R.string.login_error))
                is UiState.Error -> {
                    if (state.errorCode != Constants.SILENT_NAV_CODE) {
                        showLoginStatus(
                            getString(
                                R.string.login_error_msg,
                                state.message
                            )
                        )
                    }
                }

                UiState.ErrorConnection -> showLoginStatus(getString(R.string.login_error))
                else -> {}
            }
        }
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        loadingOverlay.visible(isLoading)
        loadingAnim.visible(isLoading)
    }

    private fun showLoginStatus(message: String, action: (() -> Unit)? = null) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).show()

        action?.let {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(3000L)
                action.invoke()
            }
        }
    }

    private fun setupFieldValidators() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateFields()
            }
        }

        binding.etUsername.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)
    }

    private fun validateFields() = with(binding) {
        val isUsernameValid = validateUsername()
        val isPasswordValid = validatePassword()
        tiUsername.isErrorEnabled = !isUsernameValid
        tiPassword.isErrorEnabled = !isPasswordValid
        btnLogin.isEnabled = isUsernameValid && isPasswordValid
    }

    private fun validateUsername(): Boolean = with(binding) {
        val username = etUsername.text.toString()

        val minLength = Constants.USERNAME_MIN_LENGTH

        return when {
            username.isBlank() -> {
                tiUsername.error = getString(R.string.err_username_empty)
                false
            }

            username.length < minLength -> {
                tiUsername.error = getString(R.string.err_username_invalid)
                false
            }

            else -> {
                tiUsername.error = null
                true
            }
        }
    }

    private fun validatePassword(): Boolean = with(binding) {
        val password = etPassword.text.toString()

        val minLength = Constants.PASSWORD_MIN_LENGTH

        return when {
            password.isBlank() -> {
                tiPassword.error = getString(R.string.err_password_empty)
                false
            }

            password.length < minLength -> {
                tiPassword.error = getString(R.string.err_password_invalid)
                false
            }

            else -> {
                tiPassword.error = null
                true
            }
        }
    }

    private fun hideKeyboard() {
        val view = activity?.currentFocus
        view?.let {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            it.clearFocus()

            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}
