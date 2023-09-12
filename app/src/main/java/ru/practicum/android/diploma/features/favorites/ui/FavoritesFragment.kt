package ru.practicum.android.diploma.features.favorites.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.features.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.features.favorites.presentation.models.FavoritesScreenState
import ru.practicum.android.diploma.features.similarvacancies.presentation.models.VacancySimilarShortUiModel
import ru.practicum.android.diploma.features.similarvacancies.ui.adapters.SimilarVacanciesAdapter
import ru.practicum.android.diploma.features.vacancydetails.ui.VacancyDetailsFragment
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.util.debounce

class FavoritesFragment : Fragment() {

    private val viewModel by viewModel<FavoritesViewModel>()

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private var adapter: SimilarVacanciesAdapter? = null
    private lateinit var onListItemClickDebounce: (VacancySimilarShortUiModel) -> Unit

    private lateinit var foundVacancies: List<VacancySimilarShortUiModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

        viewModel.getFavorites()
        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroy() {
        _binding = null
        adapter = null
        super.onDestroy()
    }

    private fun render(screenState: FavoritesScreenState) {
        when(screenState) {
            is FavoritesScreenState.Content -> renderContent(screenState.favoriteVacancies)
            is FavoritesScreenState.Loading -> renderLoading()
            is FavoritesScreenState.NothingFound -> renderNothingFound()
            is FavoritesScreenState.Error -> renderError(screenState.errorCode)
        }
    }

    private fun renderContent(favoriteVacancies: List<VacancySimilarShortUiModel>) {
        foundVacancies = favoriteVacancies
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
        adapter?.updateAdapter(emptyList())
        showPlaceHolder()
    }

    private fun renderError(errorCode: NetworkResultCode?) {
        adapter?.updateAdapter(emptyList())
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
                    R.id.action_favoritesFragment_to_vacancyDetailsFragment,
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
        binding.favoriteVacanciesList.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteVacanciesList.adapter = adapter
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