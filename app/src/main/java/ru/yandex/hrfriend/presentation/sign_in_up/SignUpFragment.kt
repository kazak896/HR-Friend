package ru.yandex.hrfriend.presentation.sign_in_up


import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.yandex.hrfriend.R
import ru.yandex.hrfriend.databinding.FragmentSignUpBinding
import ru.yandex.hrfriend.presentation.sign_in_up.util.LoginEvent
import ru.yandex.hrfriend.presentation.sign_in_up.util.SignUpEvent
@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val viewModel by viewModels<SignUpViewModel>()
    private lateinit var binding: FragmentSignUpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)
        binding.textSignIn.setOnClickListener { navigateToSignUp(view) }
        binding.btnSignUp.setOnClickListener { signUp() }
        observeSignUp(view)
    }

    private fun observeSignUp(view: View) {
        collectLatestLifecycleFlow(viewModel.eventFlow) {event ->
            when(event){
                is SignUpEvent.Success -> {
                    binding.progressBar.visibility = View.GONE
                    navigateToTabs(view)
                }
                is SignUpEvent.Failure -> {
                    Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                }
                is SignUpEvent.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun signUp() {



        viewModel.signUp(
            binding.inputName.text.toString(),
            binding.inputLastName.text.toString(),
            binding.inputEmail.text.toString(),
            binding.inputPassword.text.toString()
        )
    }
    private fun navigateToTabs(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_tabsFragment)
    }


    private fun navigateToSignUp(view: View) {
        Navigation.findNavController(view).navigate(R.id.navigateToSignIn)
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