package ru.yandex.hrfriend.presentation.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.yandex.hrfriend.R
import ru.yandex.hrfriend.databinding.FragmentProfileBinding
import ru.yandex.hrfriend.presentation.chat.UsernameFragmentDirections
import ru.yandex.hrfriend.util.Constants
import ru.yandex.hrfriend.util.PreferencesManager
import ru.yandex.hrfriend.util.findTopNavController
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private val personViewModel by viewModels<PersonProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View) {
        binding = FragmentProfileBinding.bind(view)
        if (!preferencesManager.getString(Constants.ROLE).equals("USER")) {
            binding.llForAdmin.visibility = View.VISIBLE
            binding.cl4.visibility = View.GONE
            binding.tvResumeLink.visibility = View.GONE
        } else {
            binding.llForAdmin.visibility = View.GONE
            binding.cl4.visibility = View.VISIBLE
            binding.tvResumeLink.visibility = View.VISIBLE
            binding.etResumeLink.setText(preferencesManager.getString(Constants.PATH_LINK))
        }
        binding.etName.setText(preferencesManager.getString(Constants.USERNAME))
        binding.etLastname.setText(preferencesManager.getString(Constants.LASTNAME))
        binding.etEmail.setText(preferencesManager.getString(Constants.EMAIL))

        observeSaveLink()

        binding.btnLogout.setOnClickListener { logout() }
        binding.btnAddVacancy.setOnClickListener { btnAddVacancyPressed(view) }
        binding.etResumeLink.addTextChangedListener {
            if (!it.isNullOrEmpty()) {
                binding.btnSaveLink.visibility = View.VISIBLE
            } else {
                binding.btnSaveLink.visibility = View.INVISIBLE
            }
        }
        binding.btnSaveLink.setOnClickListener {
            personViewModel.saveResumeLink(binding.etResumeLink.text.toString())
            preferencesManager.putString(Constants.PATH_LINK, binding.etResumeLink.text.toString())
        }

    }

    private fun btnAddVacancyPressed(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_addVacancyFragment2)
    }

    fun observeSaveLink() {
        collectLatestLifecycleFlow(personViewModel.saveFlow) {
            when (it) {
                is UpdateEvent.Empty -> {

                }
                is UpdateEvent.Failure -> {
                    showToast(it.errorText.toString())
                }
                is UpdateEvent.Loading -> TODO()
                is UpdateEvent.Success -> {
                    showToast("Ссылка сохранена успешно !")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
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
    private fun logout() {
        preferencesManager.clear()
        findTopNavController().navigate(R.id.signInFragment, null, navOptions {
            popUpTo(R.id.tabsFragment) {
                inclusive = true
            }
        })
    }


}