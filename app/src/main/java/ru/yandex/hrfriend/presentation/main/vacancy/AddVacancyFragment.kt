package ru.yandex.hrfriend.presentation.main.vacancy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.yandex.hrfriend.R
import ru.yandex.hrfriend.databinding.FragmentAddVacancyBinding
import ru.yandex.hrfriend.databinding.FragmentAddVacancyTypeBinding
import ru.yandex.hrfriend.presentation.main.vacancy.events.AddVacancyTypeEvent
import ru.yandex.hrfriend.presentation.main.vacancy.events.GetVacancyTypesEvent
import ru.yandex.hrfriend.util.PreferencesManager
import javax.inject.Inject

@AndroidEntryPoint
class AddVacancyFragment : Fragment(R.layout.fragment_add_vacancy) {

    private lateinit var binding: FragmentAddVacancyBinding

    @Inject
    lateinit var preferencesManager: PreferencesManager

    val vacancyViewModel by viewModels<VacancyViewModel>()
    private val vacancyTypeViewModel by viewModels<VacancyTypeViewModel>()

    private lateinit var botSheetBinding: FragmentAddVacancyTypeBinding
    private var dialogAddVT: BottomSheetDialog? = null

    var vacancyTypes = arrayOf("java", "php")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View) {
        binding = FragmentAddVacancyBinding.bind(view)

        binding.btnBack.setOnClickListener { btnBackPressed() }
        binding.btnAddVacancyType.setOnClickListener { btnAddVacancyTypeClick() }

        var arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_vacancy_spiner, vacancyTypes)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner = Spinner(requireContext())
        spinner.id = "NewSpinerId"


        with(binding.spinVacancyType)

        observeSaveVacancyType()

    }

    private fun btnAddVacancyTypeClick() {
        if (dialogAddVT == null) {
            dialogAddVT = BottomSheetDialog(requireContext())
            botSheetBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.fragment_add_vacancy_type,
                requireActivity().findViewById(R.id.bot_sheet_container),
                false
            )
            dialogAddVT!!.setContentView(botSheetBinding.root)
            botSheetBinding.btnBack.setOnClickListener { dialogAddVT!!.dismiss() }
            botSheetBinding.btnSave.setOnClickListener { saveVacancyType() }
            getVacancyTypes()
        }
        val bottomSheetBehavior = dialogAddVT!!.behavior
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialogAddVT!!.show()
    }

    private fun saveVacancyType() {
        vacancyTypeViewModel.saveVacancyType(botSheetBinding.etVacancyType.text.toString())
    }

    private fun getVacancyTypes() {
        vacancyTypeViewModel.getVacancyTypes()
    }

    private fun observeSaveVacancyType() {
        collectLatestLifecycleFlow(vacancyTypeViewModel.saveFlow) {
            when (it) {
                is AddVacancyTypeEvent.Empty -> TODO()
                is AddVacancyTypeEvent.Failure -> {
                    it.errorText?.let { it1 -> showToast(it1) }
                }
                is AddVacancyTypeEvent.Loading -> TODO()
                is AddVacancyTypeEvent.Success -> {
                    showToast("Позиция \"${it.result}\", сохранена успешно !")
                    Log.d("TAG", it.result.toString())
                }
            }
        }
        collectLatestLifecycleFlow(vacancyTypeViewModel.getAllFlow) {
            when (it) {
                is GetVacancyTypesEvent.Empty -> {

                }
                is GetVacancyTypesEvent.Failure -> {
                    showToast(it.errorText.toString())
                }
                is GetVacancyTypesEvent.Loading -> TODO()
                is GetVacancyTypesEvent.Success -> {
                    showToast("YPI !")
                    Log.d("Tag", it.result.toString())
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun btnBackPressed() {
        findNavController().popBackStack()
    }

    fun addVacancy() {

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