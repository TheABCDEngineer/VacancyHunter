package ru.practicum.android.diploma.features.filters.presentation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding
import ru.practicum.android.diploma.features.filters.domain.models.Area
import ru.practicum.android.diploma.features.filters.domain.models.Industry
import ru.practicum.android.diploma.features.filters.presentation.models.CountryScreenState
import ru.practicum.android.diploma.features.filters.presentation.models.FilterScreenState
import ru.practicum.android.diploma.features.filters.presentation.models.IndustryScreenState
import ru.practicum.android.diploma.features.filters.presentation.models.RegionScreenState
import ru.practicum.android.diploma.features.filters.presentation.viewModel.FiltersViewModel
import java.util.Collections

class FiltersFragment : Fragment() {
    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FiltersViewModel>()

    private lateinit var salaryTextWatcher: TextWatcher
    private lateinit var industrySearchTextWatcher: TextWatcher
    private lateinit var regionSearchTextWatcher: TextWatcher

    private val industriesAdapter = IndustriesAdapter()
    private val countriesAdapter = CountriesAdapter()
    private val regionsAdapter = RegionsAdapter()

    private var currentScreen: FilterScreenState = FilterScreenState.MainScreen

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
        setCountryScreenListeners()
        setRegionScreenListeners()
        customizeRecyclerView()

        viewModel.industriesScreenState.observe(viewLifecycleOwner) {
            renderIndustry(it)
        }

        viewModel.countriesScreenState.observe(viewLifecycleOwner) {
            renderCountry(it)
        }

        viewModel.regionsScreenState.observe(viewLifecycleOwner) {
            renderRegion(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        salaryTextWatcher.let { binding.expectedSalary.removeTextChangedListener(it) }
        industrySearchTextWatcher.let { binding.filterIndustrySearchField.removeTextChangedListener(it) }
        regionSearchTextWatcher.let { binding.filterRegionSearchField.removeTextChangedListener(it) }

        _binding = null
    }

    private fun setMainScreenListeners() {
        salaryTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setFilterSalary(s.toString())
                setClearIconVisibility(s)
                setFilterButtonsVisibility()
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

        binding.filterWorkPlaceClear.setOnClickListener {
            viewModel.clearWorkPlace()
            regionsAdapter.clearCheckedRegion()
            setFilterButtonsVisibility()
            binding.filterMainWorkPlaceEmpty.visibility = View.VISIBLE
            binding.filterMainWorkPlaceFilled.visibility = View.GONE
        }

        binding.filterMainBack.setOnClickListener {
            navigateOut()
        }

        binding.filterIndustryClear.setOnClickListener {
            viewModel.setFilterIndustry(null)
            industriesAdapter.clearCheckedIndustry()
            setFilterButtonsVisibility()
            render(FilterScreenState.MainScreen)
        }

        binding.filterSalaryClear.setOnClickListener {
            viewModel.setFilterSalary(null)
            setFilterButtonsVisibility()
            binding.expectedSalary.text?.clear()
        }

        binding.filterMainDoNotShowWithoutSalary.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setFilterDoNotShowWithoutSalary(isChecked)
            setFilterButtonsVisibility()
        }

        binding.filterApplyButton.setOnClickListener {
            val filter = viewModel.getFilter()
        }

        binding.filterResetButton.setOnClickListener {
            viewModel.clearFilter()
            render(FilterScreenState.MainScreen)
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateOut()
                }
            }
            )
    }

    private fun setIndustryScreenListeners() {
        industrySearchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val currentIndustry = viewModel.getIndustry()
                industriesAdapter.industries.clear()
                var filteredIndustries = viewModel.filterIndustries(s.toString())
                if (currentIndustry != null) {
                    var position = 0
                    while (position < filteredIndustries.size) {
                        if (filteredIndustries[position] == currentIndustry) {
                            Collections.swap(filteredIndustries, 0, position)
                            break
                        }
                        position++
                    }
                }
                industriesAdapter.industries.addAll(filteredIndustries)
                binding.filterIndustriesRecyclerView.smoothScrollToPosition(0)
                industriesAdapter.notifyDataSetChanged()
            }
        }
        binding.filterIndustrySearchField.addTextChangedListener(industrySearchTextWatcher)

        binding.filterIndustryBack.setOnClickListener {
            navigateOut()
        }

        binding.filterIndustriesChooseButton.setOnClickListener {
            viewModel.setFilterIndustry(industriesAdapter.getCheckedIndustry())
            render(FilterScreenState.MainScreen)
        }
    }

    private fun setWorkPlaceScreenListeners() {
        binding.filterWorkPlaceBack.setOnClickListener {
            viewModel.setCountry(null)
            navigateOut()
        }

        binding.filterWorkPlaceRegionClear.setOnClickListener {
            viewModel.setRegion(null)
            regionsAdapter.clearCheckedRegion()
            render(FilterScreenState.WorkPlaceScreen)
        }

        binding.filterWorkPlaceCountryClear.setOnClickListener {
            viewModel.setCountry(null)
            render(FilterScreenState.WorkPlaceScreen)
        }

        binding.filterWorkPlaceChooseButton.setOnClickListener {
            viewModel.setFilterCountry()
            viewModel.setFilterRegion()
            render(FilterScreenState.MainScreen)
        }
    }

    private fun setCountryScreenListeners() {
        binding.filterCountryBack.setOnClickListener {
            navigateOut()
        }

        binding.filterWorkPlaceCountryEmpty.setOnClickListener {
            filterWorkPlaceCountryClickListener()
        }

        binding.filterWorkPlaceCountryFilled.setOnClickListener {
            filterWorkPlaceCountryClickListener()
        }
    }

    private fun setRegionScreenListeners() {
        regionSearchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val currentRegion = viewModel.getRegion()
                regionsAdapter.regions.clear()
                var filteredRegions = viewModel.filterRegions(s.toString())
                if (currentRegion != null) {
                    var position = 0
                    while (position < filteredRegions.size) {
                        if (filteredRegions[position] == currentRegion) {
                            Collections.swap(filteredRegions, 0, position)
                            break
                        }
                        position++
                    }
                }
                regionsAdapter.regions.addAll(filteredRegions)
                binding.filterRegionsRecyclerView.smoothScrollToPosition(0)
                regionsAdapter.notifyDataSetChanged()
            }
        }
        binding.filterRegionSearchField.addTextChangedListener(regionSearchTextWatcher)

        binding.filterRegionBack.setOnClickListener {
            navigateOut()
        }

        binding.filterRegionsChooseButton.setOnClickListener {
            viewModel.setRegion(regionsAdapter.getCheckedArea())
            render(FilterScreenState.WorkPlaceScreen)
        }

        binding.filterWorkPlaceRegionEmpty.setOnClickListener {
            filterWorkPlaceRegionClickListener()
        }

        binding.filterWorkPlaceRegionFilled.setOnClickListener {
            filterWorkPlaceRegionClickListener()
        }
    }

    private fun customizeRecyclerView() {
        binding.filterIndustriesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.filterIndustriesRecyclerView.adapter = industriesAdapter

        binding.filterCountriesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.filterCountriesRecyclerView.adapter = countriesAdapter

        binding.filterRegionsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.filterRegionsRecyclerView.adapter = regionsAdapter


        industriesAdapter.onItemClick = { _ ->
            binding.filterIndustriesChooseButton.visibility = View.VISIBLE
        }

        countriesAdapter.onItemClick = { country ->
            viewModel.setCountry(country)
            render(FilterScreenState.WorkPlaceScreen)
        }

        regionsAdapter.onItemClick = { region ->
            binding.filterRegionsChooseButton.visibility = View.VISIBLE
        }
    }

    private fun filterMainIndustryClickListener() {
        binding.filterIndustriesChooseButton.visibility = View.GONE
        industriesAdapter.reload()
        viewModel.getIndustries()
        render(FilterScreenState.IndustryScreen)
    }

    private fun filterMainWorkPlaceClickListener() {
        render(FilterScreenState.WorkPlaceScreen)
    }

    private fun filterWorkPlaceCountryClickListener() {
        countriesAdapter.countries.clear()
        countriesAdapter.notifyDataSetChanged()
        viewModel.getCountries()
        render(FilterScreenState.CountryScreen)
    }

    private fun filterWorkPlaceRegionClickListener() {
        binding.filterRegionsChooseButton.visibility = View.GONE
        regionsAdapter.reload()
        viewModel.getRegions()
        render(FilterScreenState.RegionScreen)
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
        currentScreen = state
        when (state) {
            is FilterScreenState.MainScreen -> showMain()
            is FilterScreenState.IndustryScreen -> showIndustry()
            is FilterScreenState.WorkPlaceScreen -> showWorkPlace()
            is FilterScreenState.CountryScreen -> showCountry()
            is FilterScreenState.RegionScreen -> showRegion()
        }
    }

    private fun renderIndustry(state: IndustryScreenState) {
        when (state) {
            is IndustryScreenState.Content -> showIndustryContent(state.industries)
            is IndustryScreenState.Error -> showIndustryError()
            is IndustryScreenState.Loading -> showIndustryLoading()
        }
    }

    private fun renderCountry(state: CountryScreenState) {
        when (state) {
            is CountryScreenState.Content -> showCountryContent(state.countries)
            is CountryScreenState.Error -> showCountryError()
            is CountryScreenState.Loading -> showCountryLoading()
        }
    }

    private fun renderRegion(state: RegionScreenState) {
        when (state) {
            is RegionScreenState.Content -> showRegionContent(state.regions)
            is RegionScreenState.Error -> showRegionError()
            is RegionScreenState.Loading -> showRegionLoading()
        }
    }

    private fun showMain() {
        binding.filterMainLayout.visibility = View.VISIBLE
        binding.filterIndustryLayout.visibility = View.GONE
        binding.filterWorkPlaceLayout.visibility = View.GONE
        if (viewModel.getIndustry() != null) {
            binding.filterMainIndustryEmpty.visibility = View.GONE
            binding.filterMainIndustryFilled.visibility = View.VISIBLE
            binding.filterMainIndustry.text = viewModel.getIndustry()?.name
        } else {
            binding.filterMainIndustryEmpty.visibility = View.VISIBLE
            binding.filterMainIndustryFilled.visibility = View.GONE
        }

        val workPlaceText = viewModel.getWorkPlace()
        if (workPlaceText.isEmpty()) {
            binding.filterMainWorkPlaceEmpty.visibility = View.VISIBLE
            binding.filterMainWorkPlaceFilled.visibility = View.GONE
        } else {
            binding.filterMainWorkPlaceFilled.visibility = View.VISIBLE
            binding.filterMainWorkPlaceEmpty.visibility = View.GONE
            binding.filterMainWorkPlace.text = workPlaceText
        }

        binding.expectedSalary.setText(viewModel.getSalary())
        binding.filterMainDoNotShowWithoutSalary.isChecked = viewModel.getDoNotShowWithoutSalary()

        setFilterButtonsVisibility()
    }

    private fun showIndustry() {
        binding.filterMainLayout.visibility = View.GONE
        binding.filterWorkPlaceLayout.visibility = View.GONE
        binding.filterIndustryLayout.visibility = View.VISIBLE
        binding.filterIndustrySearchField.text.clear()
    }

    private fun showIndustryContent(industries: List<Industry>) {
        val currentIndustry = viewModel.getIndustry()
        binding.filterIndustriesProgressBar.visibility = View.GONE
        industriesAdapter.industries = industries.toMutableList()
        if (currentIndustry != null) {
            var position = 0
            while (position < industriesAdapter.industries.size) {
                if (industriesAdapter.industries[position] == currentIndustry) {
                    Collections.swap(industriesAdapter.industries, 0, position)
                    break
                }
                position++
            }
        }
        binding.filterIndustriesRecyclerView.smoothScrollToPosition(0)
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
        binding.filterCountryLayout.visibility = View.GONE
        binding.filterRegionLayout.visibility = View.GONE

        if (viewModel.getCountry() != null) {
            binding.filterWorkPlaceCountryEmpty.visibility = View.GONE
            binding.filterWorkPlaceCountryFilled.visibility = View.VISIBLE
            binding.filterWorkPlaceCountry.text = viewModel.getCountry()?.name
        } else {
            binding.filterWorkPlaceCountryEmpty.visibility = View.VISIBLE
            binding.filterWorkPlaceCountryFilled.visibility = View.GONE
        }

        if (viewModel.getRegion() != null) {
            binding.filterWorkPlaceRegionEmpty.visibility = View.GONE
            binding.filterWorkPlaceRegionFilled.visibility = View.VISIBLE
            binding.filterWorkPlaceRegion.text = viewModel.getRegion()?.name
        } else {
            binding.filterWorkPlaceRegionEmpty.visibility = View.VISIBLE
            binding.filterWorkPlaceRegionFilled.visibility = View.GONE
        }

        setWorkPlaceChooseButtonVisibility()
    }

    private fun showCountry() {
        binding.filterCountryLayout.visibility = View.VISIBLE
        binding.filterRegionLayout.visibility = View.GONE
        binding.filterWorkPlaceLayout.visibility = View.GONE
    }

    private fun showCountryContent(countries: List<Area>) {
        binding.filterCountriesProgressBar.visibility = View.GONE
        countriesAdapter.countries = countries.toMutableList()
        countriesAdapter.notifyDataSetChanged()
    }

    private fun showCountryError() {
        binding.filterCountriesProgressBar.visibility = View.GONE
        showMessage(getString(R.string.something_went_wrong))
        render(FilterScreenState.WorkPlaceScreen)
    }

    private fun showCountryLoading() {
        binding.filterCountriesProgressBar.visibility = View.VISIBLE
    }

    private fun showRegion() {
        binding.filterRegionLayout.visibility = View.VISIBLE
        binding.filterCountryLayout.visibility = View.GONE
        binding.filterWorkPlaceLayout.visibility = View.GONE
        binding.filterRegionSearchField.text.clear()
    }

    private fun showRegionContent(regions: List<Area>) {
        val currentRegion = viewModel.getRegion()
        binding.filterRegionProgressBar.visibility = View.GONE
        regionsAdapter.regions = regions.toMutableList()
        if (currentRegion != null) {
            var position = 0
            while (position < regionsAdapter.regions.size) {
                if (regionsAdapter.regions[position] == currentRegion) {
                    Collections.swap(regionsAdapter.regions, 0, position)
                    break
                }
                position++
            }
        }
        binding.filterRegionsRecyclerView.smoothScrollToPosition(0)
        regionsAdapter.notifyDataSetChanged()
    }

    private fun showRegionError() {
        binding.filterRegionProgressBar.visibility = View.GONE
        showMessage(getString(R.string.something_went_wrong))
        render(FilterScreenState.WorkPlaceScreen)
    }

    private fun showRegionLoading() {
        binding.filterRegionProgressBar.visibility = View.VISIBLE
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

    private fun setFilterButtonsVisibility() {
        if (viewModel.isFilterButtonsAvailable())
            binding.filterButtons.visibility = View.VISIBLE
        else
            binding.filterButtons.visibility = View.GONE
    }

    private fun setWorkPlaceChooseButtonVisibility() {
        if (viewModel.isWorkPlaceChooseButtonAvailable())
            binding.filterWorkPlaceChooseButton.visibility = View.VISIBLE
        else
            binding.filterWorkPlaceChooseButton.visibility = View.GONE
    }

    private fun navigateOut() {
        when (currentScreen) {
            is FilterScreenState.MainScreen -> findNavController().navigateUp()
            is FilterScreenState.IndustryScreen -> render(FilterScreenState.MainScreen)
            is FilterScreenState.WorkPlaceScreen -> render(FilterScreenState.MainScreen)
            is FilterScreenState.CountryScreen -> render(FilterScreenState.WorkPlaceScreen)
            is FilterScreenState.RegionScreen -> render(FilterScreenState.WorkPlaceScreen)
        }
    }
}