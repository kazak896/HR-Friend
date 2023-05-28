package ru.yandex.hrfriend.presentation.main.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.yandex.hrfriend.R
import ru.yandex.hrfriend.data.dto.vacancy.Content
import ru.yandex.hrfriend.data.dto.vacancy.Pageable
import ru.yandex.hrfriend.data.dto.vacancy.VacancyRequest
import ru.yandex.hrfriend.databinding.FragmentHomeBinding
import ru.yandex.hrfriend.domain.models.home.Horizontal
import ru.yandex.hrfriend.domain.models.home.Vacancy
import ru.yandex.hrfriend.presentation.main.home.adapters.HorizontalAdapter
import ru.yandex.hrfriend.presentation.main.home.adapters.VacancyAdapter
import ru.yandex.hrfriend.presentation.main.resume_response.ResumeResponseViewModel
import ru.yandex.hrfriend.presentation.main.resume_response.events.AddResumeResponseEvent
import ru.yandex.hrfriend.presentation.main.resume_response.events.ResumeResponseEvent
import ru.yandex.hrfriend.presentation.main.vacancy.VacancyViewModel
import ru.yandex.hrfriend.presentation.main.vacancy.events.GetVacanciesEvent
import ru.yandex.hrfriend.util.Constants
import ru.yandex.hrfriend.util.PreferencesManager
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    lateinit var horizontalAdapter: HorizontalAdapter
    lateinit var vacancyAdapter: VacancyAdapter

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private val vacancyViewModel by viewModels<VacancyViewModel>()
    private val resumeResponseViewModel by viewModels<ResumeResponseViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        init()

        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            hasNotificationPermissionGranted = true
        }
    }

    private fun init() {
        horizontalAdapter = HorizontalAdapter()
        binding.rvTasks.adapter = horizontalAdapter

        vacancyAdapter = VacancyAdapter()
        binding.rvVacancy.adapter = vacancyAdapter

        if (!preferencesManager.getString(Constants.ROLE).equals("USER")) {
            binding.tvVacancy.text = "Кандидаты : "
        } else {
            observeVacancies()
            observeResumeResponse()
            vacancyViewModel.getVacancies(VacancyRequest(0, 5, emptyList()))
            resumeResponseViewModel.getByUser()
            vacancyAdapter.setOnItemClickInterface(object : VacancyAdapter.OnItemClickInterface{
                override fun onResponseClick(content: Content) {
                    resumeResponseViewModel.saveResumeResponse(
                        content.id,
                        "candidateResume-${System.currentTimeMillis()}"
                    )
                }
            })
        }
    }

    private fun observeResumeResponse() {
        collectLatestLifecycleFlow(resumeResponseViewModel.saveFlow) {
            when (it) {
                is AddResumeResponseEvent.Empty -> {
                }
                is AddResumeResponseEvent.Failure -> {
                    showToast(it.errorText.toString())
                }
                is AddResumeResponseEvent.Loading -> {
                }
                is AddResumeResponseEvent.Success -> {
                    showToast("Успешный отклик на вакансию : ${it.result?.vacancy?.position?.position}")
                    it.result?.let { it1 -> horizontalAdapter.addItem(it1) }
                }
            }
        }
        collectLatestLifecycleFlow(resumeResponseViewModel.getByUserFlow) {
            when (it) {
                is ResumeResponseEvent.Empty -> {
                }
                is ResumeResponseEvent.Failure -> {
                    showToast(it.errorText.toString())
                }
                is ResumeResponseEvent.Loading -> {
                }
                is ResumeResponseEvent.Success -> {
                    Log.d("Tag2", it.result.toString())
                    it.result?.let { it1 -> horizontalAdapter.setHorizontals(it1) }
                }
            }
        }
    }

    private fun observeVacancies() {
        collectLatestLifecycleFlow(vacancyViewModel.getAllFlow) {
            when (it) {
                is GetVacanciesEvent.Empty -> {
                }
                is GetVacanciesEvent.Failure -> {
                    showToast(it.errorText.toString())
                }
                is GetVacanciesEvent.Loading -> {
                }
                is GetVacanciesEvent.Success -> {
                    Log.d("TAG", it.result?.content.toString())
                    it.result?.let { it1 -> vacancyAdapter.setVacancies(it1.content) }
                }
            }
        }

    }

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= 33) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                        showNotificationPermissionRationale()
                    } else {
                        showSettingDialog()
                    }
                }
            } /*else {
                Toast.makeText(requireContext(), "notification permission granted", Toast.LENGTH_SHORT)
                    .show()
            }*/
        }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(requireContext(), com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:ru.yandex.hrfriend")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(requireContext(), com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    var hasNotificationPermissionGranted = false

    private fun getHorizontals() : List<Horizontal> {
        return listOf(
            Horizontal("12-02-2022", "param1"),
            Horizontal("16-11-2023", "param2"),
            Horizontal("17-05-2024", "param3"),
            Horizontal("21-08-2025", "param4")
        )
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}