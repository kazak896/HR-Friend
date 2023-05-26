package ru.yandex.hrfriend.presentation.sign_in_up

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.yandex.hrfriend.R
import ru.yandex.hrfriend.databinding.FragmentSignInBinding
import ru.yandex.hrfriend.presentation.sign_in_up.util.LoginEvent

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var binding: FragmentSignInBinding

    private val viewModel by viewModels<SignInViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)
        binding.textCreateNewAccount.setOnClickListener { navigateToSignUp(view) }
        binding.btnSignIn.setOnClickListener { signIn() }
        observeLogin(view)
    }

    private fun observeLogin(view: View) {
        collectLatestLifecycleFlow(viewModel.eventFlow) {event ->
            when(event){
                is LoginEvent.Success -> {
                    binding.progressBar.visibility = View.GONE
                    navigateToTabs(view)
                }
                is LoginEvent.Failure -> {
                    Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                }
                is LoginEvent.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
                is LoginEvent.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun signIn() {
        viewModel.signIn(binding.inputEmail.text.toString(), binding.inputPassword.text.toString())
    }


    private fun navigateToSignUp(view: View) {
        Navigation.findNavController(view).navigate(R.id.navigateToSignUp)
    }

    private fun navigateToTabs(view: View) {
        Navigation.findNavController(view).navigate(R.id.navigateToTabsFragment)
    }


    private fun <T> Fragment.collectLatestLifecycleFlow(
        flow: Flow<T>,
        collect: suspend (T) -> Unit
    ): Job {
        return lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.cancellable().collectLatest(collect)
            }
        }
    }
}