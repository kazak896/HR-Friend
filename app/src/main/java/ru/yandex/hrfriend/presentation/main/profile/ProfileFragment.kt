package ru.yandex.hrfriend.presentation.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View) {
        binding = FragmentProfileBinding.bind(view)

        binding.etName.setText(preferencesManager.getString(Constants.USERNAME))
        binding.etLastname.setText(preferencesManager.getString(Constants.LASTNAME))
        binding.etEmail.setText(preferencesManager.getString(Constants.EMAIL))

        binding.btnLogout.setOnClickListener { logout() }
        binding.btnAddVacancy.setOnClickListener { btnAddVacancyPressed(view) }
    }

    private fun btnAddVacancyPressed(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_addVacancyFragment2)
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