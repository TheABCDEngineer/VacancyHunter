package ru.practicum.android.diploma.features.filters.presentation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding
import ru.practicum.android.diploma.features.filters.domain.models.Filter
import ru.practicum.android.diploma.features.filters.domain.models.Industry
import ru.practicum.android.diploma.features.filters.presentation.models.FilterScreenState
import ru.practicum.android.diploma.features.filters.presentation.models.IndustryScreenState
import ru.practicum.android.diploma.features.filters.presentation.viewModel.FiltersViewModel

class FiltersFragment : Fragment() {
    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FiltersViewModel>()

    private lateinit var salaryTextWatcher: TextWatcher
    private lateinit var industrySearchTextWatcher: TextWatcher
    private val industriesAdapter = IndustriesAdapter()

    private val filter = Filter(null, null, null, null, false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFiltersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMainScreenListeners()
        setIndustryScreenListeners()
        setWorkPlaceScreenListeners()
        customizeRecyclerView()

        viewModel.industriesScreenState.observe(viewLifecycleOwner) {
            renderIndustry(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        salaryTextWatcher.let { binding.expectedSalary.removeTextChangedListener(it) }
        industrySearchTextWatcher.let { binding.filterIndustrySearchField.removeTextChangedListener(it) }

        _binding = null
    }

    private fun setMainScreenListeners() {
        salaryTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter.salary = s.toString()
                setClearIconVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                setEditTextColors(binding.expectedSalaryText, s)
            }
        }

        binding.expectedSalary.addTextChangedListener(salaryTextWatcher)

        binding.expectedSalary.setOnFocusChangeListener { view, isFocused ->
            if (isFocused)
                setEditTextColors(binding.expectedSalaryText, "")
        }

        binding.filterMainIndustryEmpty.setOnClickListener {
            filterMainIndustryClickListener()
        }

        binding.filterMainIndustryFilled.setOnClickListener {
            filterMainIndustryClickListener()
        }

        binding.filterMainWorkPlaceEmpty.setOnClickListener {
            filterMainWorkPlaceClickListener()
        }

        binding.filterMainWorkPlaceFilled.setOnClickListener {
            filterMainWorkPlaceClickListener()
        }

        binding.filterMainBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.filterIndustryClear.setOnClickListener {
            filter.industry = null
            render(FilterScreenState.MainScreen)
        }

        binding.filterSalaryClear.setOnClickListener {
            filter.salary = null
            binding.expectedSalary.text?.clear()
        }

        binding.filterMainDoNotShowWithoutSalary.setOnCheckedChangeListener { _, isChecked ->
            filter.doNotShowWithoutSalary = isChecked
        }
    }

    private fun setIndustryScreenListeners() {
        industrySearchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                industriesAdapter.industries.clear()
                industriesAdapter.industries.addAll(viewModel.filterIndustries(s.toString()))
                industriesAdapter.notifyDataSetChanged()
            }
        }
        binding.filterIndustrySearchField.addTextChangedListener(industrySearchTextWatcher)

        binding.filterIndustryBack.setOnClickListener {
            render(FilterScreenState.MainScreen)
        }

        binding.filterIndustriesChooseButton.setOnClickListener {
            filter.industry = industriesAdapter.getCheckedIndustry()
            render(FilterScreenState.MainScreen)
        }
    }

    private fun setWorkPlaceScreenListeners() {
        binding.filterWorkPlaceBack.setOnClickListener {
            render(FilterScreenState.MainScreen)
        }
    }

    private fun customizeRecyclerView() {
        industriesAdapter.onItemClick = { _ ->
            binding.filterIndustriesChooseButton.visibility = View.VISIBLE
        }

        binding.filterIndustriesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.filterIndustriesRecyclerView.adapter = industriesAdapter
    }

    private fun filterMainIndustryClickListener() {
        binding.filterIndustriesChooseButton.visibility = View.GONE
        industriesAdapter.reload()
        viewModel.getIndustries()
        render(FilterScreenState.IndustryScreen(filter.industry))
    }

    private fun filterMainWorkPlaceClickListener() {
        render(FilterScreenState.WorkPlaceScreen(null, null))
    }

    private fun setEditTextColors(textInputLayout: TextInputLayout, text: CharSequence?) {
        val editTextColor = if (text.toString().isEmpty()) {
            ResourcesCompat.getColorStateList(resources, R.color.edit_text_color_selector, requireContext().theme)
        } else {
            ResourcesCompat.getColorStateList(resources, R.color.edit_text_color_selector_filled, requireContext().theme)
        }

        if (editTextColor != null) {
            textInputLayout.hintTextColor = editTextColor
            textInputLayout.defaultHintTextColor = editTextColor
        }
    }

    private fun render(state: FilterScreenState) {
        when (state) {
            is FilterScreenState.MainScreen -> showMain()
            is FilterScreenState.IndustryScreen -> showIndustry(state.industry)
            is FilterScreenState.WorkPlaceScreen -> showWorkPlace()
        }
    }

    private fun renderIndustry(state: IndustryScreenState) {
        when (state) {
            is IndustryScreenState.Content -> showIndustryContent(state.industries)
            is IndustryScreenState.Error -> showIndustryError()
            is IndustryScreenState.Loading -> showIndustryLoading()
        }
    }

    private fun showMain() {
        binding.filterMainLayout.visibility = View.VISIBLE
        binding.filterIndustryLayout.visibility = View.GONE
        binding.filterWorkPlaceLayout.visibility = View.GONE
        if (filter.industry != null) {
            binding.filterMainIndustryEmpty.visibility = View.GONE
            binding.filterMainIndustryFilled.visibility = View.VISIBLE
            binding.filterMainIndustry.text = filter.industry?.name
        } else {
            binding.filterMainIndustryEmpty.visibility = View.VISIBLE
            binding.filterMainIndustryFilled.visibility = View.GONE
        }
    }

    private fun showIndustry(industry: Industry?) {
        binding.filterMainLayout.visibility = View.GONE
        binding.filterWorkPlaceLayout.visibility = View.GONE
        binding.filterIndustryLayout.visibility = View.VISIBLE
        binding.filterIndustrySearchField.text.clear()

        // TODO industry
    }

    private fun showIndustryContent(industries: List<Industry>) {
        binding.filterIndustriesProgressBar.visibility = View.GONE
        industriesAdapter.industries = industries.toMutableList()
        industriesAdapter.notifyDataSetChanged()
    }

    private fun showIndustryError() {
        binding.filterIndustriesProgressBar.visibility = View.GONE
        showMessage(getString(R.string.something_went_wrong))
        render(FilterScreenState.MainScreen)
    }

    private fun showIndustryLoading() {
        binding.filterIndustriesProgressBar.visibility = View.VISIBLE
    }



    private fun showWorkPlace() {
        binding.filterWorkPlaceLayout.visibility = View.VISIBLE
        binding.filterMainLayout.visibility = View.GONE
        binding.filterIndustryLayout.visibility = View.GONE
    }

    private fun setClearIconVisibility(text: CharSequence?) {
        binding.filterSalaryClear.visibility =
            if (text.toString().isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}