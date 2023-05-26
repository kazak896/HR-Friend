package ru.yandex.hrfriend.presentation.main.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.yandex.hrfriend.R
import ru.yandex.hrfriend.databinding.FragmentHomeBinding
import ru.yandex.hrfriend.domain.models.home.Horizontal
import ru.yandex.hrfriend.domain.models.home.Vacancy
import ru.yandex.hrfriend.presentation.main.home.adapters.HorizontalAdapter
import ru.yandex.hrfriend.presentation.main.home.adapters.VacancyAdapter


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    lateinit var horizontalAdapter: HorizontalAdapter
    lateinit var vacancyAdapter: VacancyAdapter

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
        horizontalAdapter.setHorizontals(getHorizontals())
        vacancyAdapter = VacancyAdapter()
        binding.rvVacancy.adapter = vacancyAdapter
        //vacancyAdapter.setVacancies(getVacancies())

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
            } else {
                Toast.makeText(requireContext(), "notification permission granted", Toast.LENGTH_SHORT)
                    .show()
            }
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

    /*private fun getVacancies() : List<Vacancy> {
        return listOf(
            Vacancy("Android developer", "Cheboksary", "OOO Byket Chyvashii", "10000000$"),
            Vacancy("Backend", "Moscow", "OOO Firm2", "100000$"),
            Vacancy("Full stack", "Kazan", "OOO Firm3", "10000$"),
            Vacancy("Front End", "Samara", "OOO Firm4", "10000$"),
            Vacancy("Python", "Grozny", "OOO Firm5", "1$"),
        )
    }*/

}