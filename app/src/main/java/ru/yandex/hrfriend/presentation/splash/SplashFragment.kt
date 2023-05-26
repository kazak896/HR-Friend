package ru.yandex.hrfriend.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import ru.yandex.hrfriend.MainActivity
import ru.yandex.hrfriend.MainActivityArgs
import ru.yandex.hrfriend.R
import ru.yandex.hrfriend.util.Constants
import ru.yandex.hrfriend.util.PreferencesManager
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateToMainActivity(preferencesManager.getString(Constants.JWT_KEY) != null)

    }

    private fun navigateToMainActivity(isSignedIn: Boolean) {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val args = MainActivityArgs(isSignedIn)
        intent.putExtras(args.toBundle())
        startActivity(intent)
    }


}