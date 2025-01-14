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
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.features.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.features.favorites.presentation.models.FavoritesScreenState
import ru.practicum.android.diploma.features.favorites.ui.adapters.FavVacancyComparator
import ru.practicum.android.diploma.features.favorites.ui.adapters.PagedFavoritesAdapter
import ru.practicum.android.diploma.root.presentation.model.VacancyShortUiModel
import ru.practicum.android.diploma.root.data.network.models.NetworkResultCode
import ru.practicum.android.diploma.root.presentation.ui.adapters.VacancyClickListener
import ru.practicum.android.diploma.util.debounce

class FavoritesFragment : Fragment() {

    private val viewModel by viewModel<FavoritesViewModel>()

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private var pagingAdapter: PagedFavoritesAdapter? = null
    private lateinit var onListItemClickDebounce: (VacancyShortUiModel) -> Unit

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

        setPagingAdapter()

        viewModel.getPagedFavorites()

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroy() {
        _binding = null
        pagingAdapter = null
        super.onDestroy()
    }

    private fun render(screenState: FavoritesScreenState) {
        when(screenState) {
            is FavoritesScreenState.Loading -> renderLoading()
            is FavoritesScreenState.NothingFound -> renderNothingFound()
            is FavoritesScreenState.Error -> renderError(screenState.errorCode)
            is FavoritesScreenState.ContentPaged -> renderContentPaged(screenState.favoriteVacancies)
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

    private fun renderContentPaged(favoriteVacancies: PagingData<VacancyShortUiModel>) {

        viewLifecycleOwner.lifecycleScope.launch {
            pagingAdapter?.submitData(favoriteVacancies)
            binding.favoriteVacanciesList.isVisible = true
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingAdapter?.loadStateFlow?.collectLatest { loadStates ->
                when {
                    loadStates.refresh is LoadState.Loading -> renderLoading()
                    loadStates.refresh is LoadState.Error -> renderError(null)
                    loadStates.append is LoadState.Error -> renderError(null)
                    pagingAdapter?.itemCount == 0 -> renderNothingFound()
                    else -> {
                        binding.apply {
                            progressBar.isVisible = false
                            nothingFoundPlaceholder.isVisible = false
                            placeholderMessage.isVisible = false
                            favoriteVacanciesList.isVisible = true
                        }
                    }
                }
            }
        }
    }

    private fun setPagingAdapter() {
        onListItemClickDebounce = debounce<VacancyShortUiModel>(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            true
        ) { vacancy ->
            findNavController()
                .navigate(
                    FavoritesFragmentDirections.actionFavoritesFragmentToVacancyDetailsFragment(
                        vacancy.vacancyId
                    )
                )
        }

        pagingAdapter = PagedFavoritesAdapter(
            object : VacancyClickListener {
                override fun onListItemClick(vacancy: VacancyShortUiModel) {
                    onListItemClickDebounce(vacancy)
                }
            },
            FavVacancyComparator
        )
        binding.favoriteVacanciesList.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteVacanciesList.adapter = pagingAdapter
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