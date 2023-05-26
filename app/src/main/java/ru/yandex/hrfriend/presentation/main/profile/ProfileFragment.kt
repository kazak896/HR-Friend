package ru.yandex.hrfriend.presentation.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.navOptions
import dagger.hilt.android.AndroidEntryPoint
import ru.yandex.hrfriend.R
import ru.yandex.hrfriend.databinding.FragmentProfileBinding
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
        binding = FragmentProfileBinding.bind(view)

        binding.btnLogout.setOnClickListener { logout() }
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