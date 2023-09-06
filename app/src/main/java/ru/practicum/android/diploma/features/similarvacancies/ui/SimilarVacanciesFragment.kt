package ru.practicum.android.diploma.features.similarvacancies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.features.similarvacancies.presentation.SimilarVacanciesViewModel
import ru.practicum.android.diploma.features.similarvacancies.presentation.models.SimilarVacanciesState
import ru.practicum.android.diploma.features.similarvacancies.presentation.models.VacancySimilarShortUiModel
import ru.practicum.android.diploma.features.similarvacancies.ui.adapters.SimilarVacanciesAdapter
import ru.practicum.android.diploma.features.vacancydetails.ui.VacancyDetailsFragment
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.util.debounce

class SimilarVacanciesFragment : Fragment() {

    private val viewModel by viewModel<SimilarVacanciesViewModel>()

    private var _binding: FragmentSimilarVacanciesBinding? = null
    private val binding get() = _binding!!

    private var adapter: SimilarVacanciesAdapter? = null
    private lateinit var onListItemClickDebounce: (VacancySimilarShortUiModel) -> Unit

    private lateinit var foundVacancies: List<VacancySimilarShortUiModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimilarVacanciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

        viewModel.getSimilarVacancies(getIdFromArgs())

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        binding.returnArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }

    private fun render(screenState: SimilarVacanciesState) {
        when (screenState) {
            is SimilarVacanciesState.Content -> renderContent(screenState.similarVacancies)
            is SimilarVacanciesState.Loading -> renderLoading()
            is SimilarVacanciesState.NothingFound -> renderNothingFound()
            is SimilarVacanciesState.Error -> renderError(screenState.errorCode)
        }
    }

    private fun renderContent(similarVacancies: List<VacancySimilarShortUiModel>) {
        foundVacancies = similarVacancies
        adapter?.updateAdapter(foundVacancies)
        binding.apply {
            progressBar.isVisible = false
            nothingFoundPlaceholder.isVisible = false
            placeholderMessage.isVisible = false
        }
    }

    private fun renderLoading() {
        binding.apply {
            progressBar.isVisible = true
            nothingFoundPlaceholder.isVisible = false
            placeholderMessage.isVisible = false
        }
    }

    private fun renderNothingFound() {
        showPlaceHolder()
    }

    private fun renderError(errorCode: NetworkResultCode?) {
        showPlaceHolder()
        if (errorCode == null) {
            showMessage(getString(R.string.something_went_wrong))
        }
    }

    private fun setAdapter() {
        onListItemClickDebounce = debounce<VacancySimilarShortUiModel>(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            true
        ) { vacancy ->
            findNavController()
                .navigate(
                    R.id.action_similarVacanciesFragment_to_vacancyDetailsFragment,
                    VacancyDetailsFragment.createArgs(vacancy.vacancyId)
                )
        }

        adapter = SimilarVacanciesAdapter(
            object : SimilarVacanciesAdapter.ListItemClickListener {
                override fun onListItemClick(vacancy: VacancySimilarShortUiModel) {
                    onListItemClickDebounce(vacancy)
                }
            }
        )
        binding.similarVacanciesList.layoutManager = LinearLayoutManager(requireContext())
        binding.similarVacanciesList.adapter = adapter
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun showPlaceHolder() {
        binding.apply {
            progressBar.isVisible = false
            nothingFoundPlaceholder.isVisible = true
            placeholderMessage.isVisible = true
        }
    }

    private fun getIdFromArgs(): String {
        return requireArguments().getString(ARGS_VACANCY_ID) ?: ""
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 300L
        private const val ARGS_VACANCY_ID = "ARGS_VACANCY_ID"
        fun createArgs(id: String): Bundle =
            bundleOf(ARGS_VACANCY_ID to id)
    }

}