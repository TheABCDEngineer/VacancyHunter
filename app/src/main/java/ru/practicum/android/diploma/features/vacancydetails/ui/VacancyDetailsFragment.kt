package ru.practicum.android.diploma.features.vacancydetails.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.text.Html
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancydetailsBinding
import ru.practicum.android.diploma.features.similarvacancies.ui.SimilarVacanciesFragment
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Email
import ru.practicum.android.diploma.features.vacancydetails.presentation.VacancyDetailsViewModel
import ru.practicum.android.diploma.features.vacancydetails.presentation.models.VacancyDetailsEvent
import ru.practicum.android.diploma.features.vacancydetails.presentation.models.VacancyDetailsState
import ru.practicum.android.diploma.features.vacancydetails.presentation.models.VacancyDetailsUiModel
import ru.practicum.android.diploma.features.vacancydetails.ui.adapters.PhonesAdapter
import ru.practicum.android.diploma.util.debounce
import ru.practicum.android.diploma.util.isInternetConnected

class VacancyDetailsFragment : Fragment() {
    private val viewModel by viewModel<VacancyDetailsViewModel>()
    private val externalNavigator: ExternalNavigator by inject()

    private var _binding: FragmentVacancydetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var vacancy: VacancyDetailsUiModel
    private var phonesAdapter: PhonesAdapter? = null
    private lateinit var onListItemClickDebounce: (String) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancydetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getVacancyById(getIdFromArgs())

        viewModel.screenState.observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.externalNavEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { action ->
                when (action) {
                    is VacancyDetailsEvent.ComposeEmail -> composeEmail(action.email)
                    is VacancyDetailsEvent.ShareVacancy -> shareVacancy(action.sharingText)
                }
            }
        }

        setClickListeners()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        phonesAdapter = null
    }

    private fun render(screenState: VacancyDetailsState) {
        when (screenState) {
            is VacancyDetailsState.Content -> renderContent(screenState.vacancy)
            is VacancyDetailsState.Loading -> renderLoading()
            is VacancyDetailsState.Error -> renderError()
            is VacancyDetailsState.ToggleFavorite -> renderFavorite(screenState.isFavorite)
        }
    }

    private fun renderContent(foundVacancy: VacancyDetailsUiModel) {
        vacancy = foundVacancy
        setPhonesAdapter()
        binding.progressBar.isVisible = false

        renderVacancyName()
        renderSalary()
        renderEmployerCard()
        renderExperience()
        renderEmployment()
        renderDescription()
        renderKeySkills()
        renderContacts()
        renderFavorite(foundVacancy.isFavorite)

        binding.similarVacanciesButton.isVisible = isInternetConnected(requireContext())

    }

    private fun renderFavorite(isFavorite: Boolean) {
        when (isFavorite) {
            true -> binding.favButton.setImageResource(R.drawable.ic_favorites_on)
            false -> binding.favButton.setImageResource(R.drawable.ic_favorites_off)
        }
    }

    private fun renderContacts() {
        if (vacancy.contactsPhones.isNotEmpty() || vacancy.contactsName.isNotEmpty() || vacancy.contactsEmail.isNotEmpty()) {
            binding.contactsTitle.isVisible = true
        }
        if (vacancy.contactsName.isNotEmpty()) {
            binding.contactsName.text = vacancy.contactsName
            binding.contactsPersonTitle.isVisible = true
            binding.contactsName.isVisible = true
        }
        if (vacancy.contactsEmail.isNotEmpty()) {
            binding.email.text = vacancy.contactsEmail
            binding.emailTitle.isVisible = true
            binding.email.isVisible = true
        }
    }

    private fun renderKeySkills() {
        if (vacancy.keySkills.isNotEmpty()) {
            binding.keySkills.text = vacancy.keySkills
            binding.keySkillsTitle.isVisible = true
            binding.keySkills.isVisible = true
        }
    }

    private fun renderDescription() {
        if (vacancy.vacancyDescription.isNotEmpty()) {

            binding.vacancyDescription.text =
                Html.fromHtml(vacancy.vacancyDescription, Html.FROM_HTML_MODE_COMPACT)

            binding.descriptionTitle.isVisible = true
            binding.vacancyDescription.isVisible = true
        }
    }

    private fun renderEmployment() {
        if (vacancy.employmentTypes.isNotEmpty()) {
            binding.employment.text = vacancy.employmentTypes
            binding.employment.isVisible = true
        }
    }

    private fun renderExperience() {
        if (vacancy.experience.isNotEmpty()) {
            binding.experience.text = vacancy.experience
            binding.experienceReq.isVisible = true
            binding.experience.isVisible = true
        }
    }

    private fun renderEmployerCard() {
        val cardIsEmpty = vacancy.logoUrl.isEmpty() && vacancy.employerName.isEmpty() && vacancy.employerAddress.isEmpty()
        if (cardIsEmpty) {
            binding.employerCard.isVisible = false
            return
        }

        binding.employerCard.isVisible = true

        if (vacancy.logoUrl.isNotEmpty()) {
            Glide.with(binding.logoImage)
                .load(vacancy.logoUrl)
                .centerInside()
                .transform(RoundedCorners(dpToPx(R.dimen.logo_corner_radius)))
                .placeholder(R.drawable.placeholder)
                .into(binding.logoImage)
        }
        if (vacancy.employerName.isNotEmpty()) {
            binding.employerName.text = vacancy.employerName
            binding.employerName.isVisible = true
        }
        if (vacancy.employerAddress.isNotEmpty()) {
            binding.city.text = vacancy.employerAddress
            binding.city.isVisible = true
        }
    }

    private fun renderSalary() {
        if (vacancy.salary.isNotEmpty()) {
            binding.salary.text = vacancy.salary
            binding.salary.isVisible = true
        }
    }

    private fun renderVacancyName() {
        if (vacancy.vacancyName.isNotEmpty()) {
            binding.vacancyName.text = vacancy.vacancyName
            binding.vacancyName.isVisible = true
        }
    }

    private fun setPhonesAdapter() {
        onListItemClickDebounce = debounce<String>(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            true
        ) { phoneNumber ->
            val intent = externalNavigator.getDialIntent(phoneNumber)
            tryStartActivity(intent)
        }

        phonesAdapter = PhonesAdapter(
            phones = vacancy.contactsPhones,

            object : PhonesAdapter.ListItemClickListener {
                override fun onPhoneClick(phoneNumber: String) {
                    onListItemClickDebounce(phoneNumber)
                }
            }
        )

        binding.phonesList.layoutManager = LinearLayoutManager(requireContext())
        binding.phonesList.adapter = phonesAdapter
    }

    private fun renderLoading() {
        binding.progressBar.isVisible = true
    }

    private fun renderError() {
        showMessage(getString(R.string.something_went_wrong))
        findNavController().navigateUp()
    }

    private fun setClickListeners() {
        binding.returnArrow.setOnClickListener { findNavController().navigateUp() }

        binding.shareButton.setOnClickListener {
            generateShareText()
        }

        binding.favButton.setOnClickListener { viewModel.toggleFavorites() }

        binding.similarVacanciesButton.setOnClickListener {
            if (isInternetConnected(requireContext())) {
                findNavController().navigate(
                    R.id.action_vacancyDetailsFragment_to_similarVacanciesFragment,
                    SimilarVacanciesFragment.createArgs(vacancy.vacancyId)
                )
            } else {
                binding.similarVacanciesButton.isVisible = false
                showMessage(getString(R.string.no_internet_connection))
            }
        }

        binding.email.setOnClickListener {
            viewModel.composeEmail(vacancy.contactsEmail, vacancy.vacancyName)
        }
    }

    private fun generateShareText() {
        val strings = listOf(
            vacancy.vacancyName,
            vacancy.salary,
            vacancy.employerName,
            vacancy.employerAddress,
            vacancy.shareVacancyUrl
        )
        viewModel.generateShareText(strings)
    }

    private fun shareVacancy(message: String) {
        if (message.isEmpty()) {
            showMessage(getString(R.string.empty_share_text))
        } else {
            val chooserMessage = getString(R.string.share_to)
            val intent = externalNavigator.getShareIntent(message, chooserMessage)
            tryStartActivity(intent)
        }
    }

    private fun composeEmail(email: Email) {
        val intent = externalNavigator.getEmailIntent(email)
        tryStartActivity(intent)
    }

    private fun tryStartActivity(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showMessage(getString(R.string.no_app_found))
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun getIdFromArgs(): String {
        return requireArguments().getString(ARGS_VACANCY_ID) ?: ""
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 300L
        private const val ARGS_VACANCY_ID = "ARGS_VACANCY_ID"
        fun createArgs(id: String): Bundle = bundleOf(ARGS_VACANCY_ID to id)
    }
}