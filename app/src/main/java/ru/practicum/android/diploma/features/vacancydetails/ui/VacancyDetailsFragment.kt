package ru.practicum.android.diploma.features.vacancydetails.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.text.Html
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancydetailsBinding
import ru.practicum.android.diploma.features.vacancydetails.domain.models.Email
import ru.practicum.android.diploma.features.vacancydetails.presentation.VacancyDetailsViewModel
import ru.practicum.android.diploma.features.vacancydetails.presentation.models.VacancyDetailsEvent
import ru.practicum.android.diploma.features.vacancydetails.presentation.models.VacancyDetailsState
import ru.practicum.android.diploma.features.vacancydetails.presentation.models.VacancyDetailsUiModel
import ru.practicum.android.diploma.features.vacancydetails.ui.adapters.PhonesAdapter
import ru.practicum.android.diploma.util.debounce
import ru.practicum.android.diploma.util.isInternetConnected

class VacancyDetailsFragment : Fragment() {

    private val arguments:VacancyDetailsFragmentArgs by navArgs()

    private val viewModel by viewModel<VacancyDetailsViewModel>()
    private val externalNavigator: ExternalNavigator by inject()

    private var _binding: FragmentVacancydetailsBinding? = null
    private val binding get() = _binding!!

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

        viewModel.getVacancyById(arguments.vacancyId)

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
            is VacancyDetailsState.ToggleFavorite -> {
                renderFavorite(screenState.isFavorite)
            }
        }
    }

    private fun renderContent(foundVacancy: VacancyDetailsUiModel) {

        setPhonesAdapter(foundVacancy)

        binding.progressBar.isVisible = false

        renderVacancyName(foundVacancy)
        renderSalary(foundVacancy)
        renderEmployerCard(foundVacancy)
        renderExperience(foundVacancy)
        renderEmployment(foundVacancy)
        renderDescription(foundVacancy)
        renderKeySkills(foundVacancy)
        renderContacts(foundVacancy)
        renderFavorite(foundVacancy.isFavorite)
        renderTopBarButtons()

        setClickListeners(foundVacancy)

        binding.similarVacanciesButton.isVisible = isInternetConnected(requireContext())

    }

    private fun renderTopBarButtons() {
        binding.favButton.isVisible = true
        binding.shareButton.isVisible = true
    }

    private fun renderFavorite(isFavorite: Boolean) {
        when (isFavorite) {
            true -> {
                binding.favButton.setImageResource(R.drawable.ic_favorites_on)
                binding.favButton.setColorFilter(requireContext().getColor(R.color.yp_red))
            }
            false -> {
                binding.favButton.setImageResource(R.drawable.ic_favorites_off)
                binding.favButton.clearColorFilter()
            }
        }
    }

    private fun renderContacts(vacancy: VacancyDetailsUiModel) {
        if (
            vacancy.contactsPhones.isNotEmpty() ||
            vacancy.contactsName.isNotEmpty() ||
            vacancy.contactsEmail.isNotEmpty()
            ) {
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

    private fun renderKeySkills(vacancy: VacancyDetailsUiModel) {
        if (vacancy.keySkills.isNotEmpty()) {
            binding.keySkills.text = vacancy.keySkills
            binding.keySkillsTitle.isVisible = true
            binding.keySkills.isVisible = true
        }
    }

    private fun renderDescription(vacancy: VacancyDetailsUiModel) {
        if (vacancy.vacancyDescription.isNotEmpty()) {

            binding.vacancyDescription.text =
                Html.fromHtml(vacancy.vacancyDescription, Html.FROM_HTML_MODE_COMPACT)

            binding.descriptionTitle.isVisible = true
            binding.vacancyDescription.isVisible = true
        }
    }

    private fun renderEmployment(vacancy: VacancyDetailsUiModel) {
        if (vacancy.employmentTypes.isNotEmpty()) {
            binding.employment.text = vacancy.employmentTypes
            binding.employment.isVisible = true
        }
    }

    private fun renderExperience(vacancy: VacancyDetailsUiModel) {
        if (vacancy.experience.isNotEmpty()) {
            binding.experience.text = vacancy.experience
            binding.experienceReq.isVisible = true
            binding.experience.isVisible = true
        }
    }

    private fun renderEmployerCard(vacancy: VacancyDetailsUiModel) {
        val cardIsEmpty: Boolean =
            vacancy.logoUrl.isEmpty() &&
            vacancy.employerName.isEmpty() &&
            vacancy.employerAddress.isEmpty()
        if (cardIsEmpty) {
            binding.employerCard.isVisible = false
            return
        }

        binding.employerCard.isVisible = true

        if (vacancy.logoUrl.isNotEmpty()) {
            Glide.with(binding.logoImage)
                .load(vacancy.logoUrl)
                .centerInside()
                .transform(RoundedCorners(
                    resources.getDimensionPixelSize(R.dimen.logo_corner_radius)
                ))
                .placeholder(R.drawable.placeholder)
                .into(binding.logoImage)
        }
        if (vacancy.employerName.isNotEmpty()) {
            binding.employerName.text = vacancy.employerName
            binding.employerName.isVisible = true
        }
        if (vacancy.employerAddress.isNotEmpty()) {
            binding.address.text = vacancy.employerAddress
            binding.address.isVisible = true
        }
    }

    private fun renderSalary(vacancy: VacancyDetailsUiModel) {
        if (vacancy.salary.isNotEmpty()) {
            binding.salary.text = vacancy.salary
            binding.salary.isVisible = true
        }
    }

    private fun renderVacancyName(vacancy: VacancyDetailsUiModel) {
        if (vacancy.vacancyName.isNotEmpty()) {
            binding.vacancyName.text = vacancy.vacancyName
            binding.vacancyName.isVisible = true
        }
    }

    private fun setPhonesAdapter(vacancy: VacancyDetailsUiModel) {
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

    private fun setClickListeners(vacancy: VacancyDetailsUiModel) {
        binding.returnArrow.setOnClickListener { findNavController().navigateUp() }

        binding.shareButton.setOnClickListener {
            generateShareText(vacancy)
        }

        binding.favButton.setOnClickListener { viewModel.toggleFavorites() }

        binding.similarVacanciesButton.setOnClickListener {
            if (isInternetConnected(requireContext())) {
                findNavController().navigate(
                    VacancyDetailsFragmentDirections.actionVacancyDetailsFragmentToSimilarVacanciesFragment(
                        arguments.vacancyId
                    )
                )
            } else {
                binding.similarVacanciesButton.isVisible = false
                showMessage(getString(R.string.no_internet_connection))
            }
        }

        binding.email.setOnClickListener {
            viewModel.composeEmail()
        }
    }

    private fun generateShareText(vacancy: VacancyDetailsUiModel) {
        viewModel.generateShareText(vacancy.salary, vacancy.employerAddress)
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

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 300L
    }
}