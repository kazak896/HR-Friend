package ru.yandex.hrfriend.presentation.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.yandex.hrfriend.R
import ru.yandex.hrfriend.databinding.FragmentUsernameBinding
import ru.yandex.hrfriend.util.Constants
import ru.yandex.hrfriend.util.PreferencesManager
import javax.inject.Inject

@AndroidEntryPoint
class UsernameFragment : Fragment(R.layout.fragment_username) {

    private lateinit var binding: FragmentUsernameBinding

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUsernameBinding.bind(view)

        binding.btnChat.setOnClickListener{
            navigateToChat("${preferencesManager.getString(Constants.USERNAME)} ${preferencesManager.getString(Constants.LASTNAME)}")
        }
    }

    private fun navigateToChat(username: String) {
        val direction = UsernameFragmentDirections.navigateToChat(username)
        findNavController().navigate(direction)
    }
}