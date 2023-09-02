package ru.practicum.android.diploma.features.similarvacancies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.features.similarvacancies.domain.models.VacancyShortSimilar
import ru.practicum.android.diploma.features.similarvacancies.presentation.SimilarVacanciesViewModel
import ru.practicum.android.diploma.features.similarvacancies.presentation.models.SimilarVacanciesState
import ru.practicum.android.diploma.features.similarvacancies.presentation.models.SimilarVacancyUiModel
import ru.practicum.android.diploma.features.similarvacancies.ui.adapters.SimilarVacanciesAdapter
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode

class SimilarVacanciesFragment : Fragment() {

    private val viewModel by viewModel<SimilarVacanciesViewModel>()

    private var _binding: FragmentSimilarVacanciesBinding? = null
    val binding get() = _binding!!

    private var adapter: SimilarVacanciesAdapter? = null

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

        adapter

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is SimilarVacanciesState.Content -> renderContent(it.similarVacancies)
                is SimilarVacanciesState.Loading -> showProgressBar()
                is SimilarVacanciesState.NothingFound -> showPlaceHolder()
                is SimilarVacanciesState.Error -> showError(it.errorCode)
            }
        }
    }

    private fun showError(errorCode: NetworkResultCode?) {

    }

    private fun showPlaceHolder() {

    }

    private fun showProgressBar() {

    }

    private fun renderContent(similarVacancies: List<VacancyShortSimilar>) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }
}