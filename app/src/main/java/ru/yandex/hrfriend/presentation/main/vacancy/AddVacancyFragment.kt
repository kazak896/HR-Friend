package ru.yandex.hrfriend.presentation.main.vacancy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import ru.yandex.hrfriend.data.dto.vacancy_type.VacancyType
import ru.yandex.hrfriend.data.dto.vacancy_type.toPosition
import ru.yandex.hrfriend.databinding.FragmentAddVacancyBinding
import ru.yandex.hrfriend.databinding.FragmentAddVacancyTypeBinding
import ru.yandex.hrfriend.presentation.main.vacancy.events.AddVacancyEvent
import ru.yandex.hrfriend.presentation.main.vacancy.events.AddVacancyTypeEvent
import ru.yandex.hrfriend.presentation.main.vacancy.events.GetVacancyTypesEvent
import ru.yandex.hrfriend.util.PreferencesManager
import javax.inject.Inject

@AndroidEntryPoint
class AddVacancyFragment : Fragment(R.layout.fragment_add_vacancy),
    AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentAddVacancyBinding

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private val vacancyViewModel by viewModels<VacancyViewModel>()
    private val vacancyTypeViewModel by viewModels<VacancyTypeViewModel>()

    private lateinit var botSheetBinding: FragmentAddVacancyTypeBinding
    private var dialogAddVT: BottomSheetDialog? = null

    private var vacancyTypesG: List<VacancyType> = emptyList()
    private var stringArray: MutableList<String> = emptyArray<String>().toMutableList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View) {
        binding = FragmentAddVacancyBinding.bind(view)

        binding.btnBack.setOnClickListener { btnBackPressed() }
        binding.btnAddVacancyType.setOnClickListener { btnAddVacancyTypeClick() }

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_vacancy_spiner, stringArray)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        with(binding.spinVacancyType) {
            adapter = arrayAdapter
            onItemSelectedListener = this@AddVacancyFragment
            prompt = "Выберите должность"
        }
        binding.btnSave.setOnClickListener { addVacancy() }

        observeVacancyType(arrayAdapter)
        observeSaveVacancy()
        getVacancyTypes()
    }

    private fun observeSaveVacancy() {
        collectLatestLifecycleFlow(vacancyViewModel.saveFlow) {
            when (it) {
                is AddVacancyEvent.Empty -> {

                }
                is AddVacancyEvent.Failure -> {
                    showToast(it.errorText.toString())
                }
                is AddVacancyEvent.Loading -> {

                }
                is AddVacancyEvent.Success -> {
                    showToast("Вакансия на позицию : ${it.result?.position?.position}, сохранена успешно !")
                }
            }
        }
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

    private fun observeVacancyType(arrayAdapter: ArrayAdapter<String>) {
        collectLatestLifecycleFlow(vacancyTypeViewModel.saveFlow) {
            when (it) {
                is AddVacancyTypeEvent.Empty -> TODO()
                is AddVacancyTypeEvent.Failure -> {
                    it.errorText?.let { it1 -> showToast(it1) }
                }

                is AddVacancyTypeEvent.Loading -> TODO()
                is AddVacancyTypeEvent.Success -> {
                    showToast("Позиция \"${it.result?.position}\", сохранена успешно !")
                    Log.d("TAG", it.result.toString())
                    getVacancyTypes()
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
                    Log.d("TAG", it.result.toString())
                    it.result?.let { vacancyTypes ->
                        vacancyTypesG = vacancyTypes
                        if (vacancyTypes.isNotEmpty()) {

                            stringArray = vacancyTypes.map { vacancyType -> vacancyType.position }
                                .toMutableList()

                            Log.d("TAG", stringArray.toString())
                            arrayAdapter.clear()
                            arrayAdapter.addAll(stringArray)
                            arrayAdapter.notifyDataSetChanged()
                        }

                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun btnBackPressed() {
        findNavController().popBackStack()
    }

    private fun addVacancy() {
        val position = findPositionByPositionName(binding.spinVacancyType.selectedItem.toString())
        val salary = binding.etSalary.text.toString()
        val yearsStart = binding.etYearsStart.text.toString()
        val yearsEnd = binding.etYearsEnd.text.toString()
        val location = binding.etLocation.text.toString()
        val description = binding.etDescription.text.toString()
        Log.d("TAG", position.toString())
        position?.toPosition()?.let {
            vacancyViewModel.saveVacancy(
                description = description,
                position = it,
                salary = salary,
                startYearsXP = yearsStart.toInt(),
                endYearsXP = yearsEnd.toInt(),
                location = location
            )
        }
    }
//    создал 2 пользака - Admin@admin.ru/admin и HR-default@hr.ru/hrhrhr дефолтные с нужными ролями
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

    private fun findPositionByPositionName(posName: String): VacancyType? {
        Log.d(posName, vacancyTypesG.toString())
        var vacancyType : VacancyType? = null
        vacancyTypesG.forEach {
            if (posName.equals(it.position)) {
                vacancyType = it
            }
        }
        return vacancyType
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        showToast(message = "Nothing selected")
    }
}