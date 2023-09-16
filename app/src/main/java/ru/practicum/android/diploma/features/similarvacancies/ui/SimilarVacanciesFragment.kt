package ru.practicum.android.diploma.features.similarvacancies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.features.similarvacancies.presentation.SimilarVacanciesViewModel
import ru.practicum.android.diploma.features.similarvacancies.presentation.models.SimilarVacanciesState
import ru.practicum.android.diploma.root.presentation.model.VacancyShortUiModel
import ru.practicum.android.diploma.root.presentation.ui.adapters.VacanciesAdapter
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.util.debounce

class SimilarVacanciesFragment : Fragment() {

    val args:SimilarVacanciesFragmentArgs by navArgs()

    private val viewModel by viewModel<SimilarVacanciesViewModel>()

    private var _binding: FragmentSimilarVacanciesBinding? = null
    private val binding get() = _binding!!

    private var adapter: VacanciesAdapter? = null
    private lateinit var onListItemClickDebounce: (VacancyShortUiModel) -> Unit

    private lateinit var foundVacancies: List<VacancyShortUiModel>

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

        viewModel.getSimilarVacancies(args.vacancyId)

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

    private fun renderContent(similarVacancies: List<VacancyShortUiModel>) {
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
        onListItemClickDebounce = debounce<VacancyShortUiModel>(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            true
        ) { vacancy ->
            findNavController()
                .navigate(
                    SimilarVacanciesFragmentDirections.actionSimilarVacanciesFragmentToVacancyDetailsFragment(
                        vacancy.vacancyId
                    )
                )
        }

        adapter = VacanciesAdapter(
            object : VacanciesAdapter.ListItemClickListener {
                override fun onListItemClick(vacancy: VacancyShortUiModel) {
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

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 300L
    }
}