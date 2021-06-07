package com.openclassrooms.realestatemanager.ui.addestate

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.DialogFragmentAddPhotoBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddPhotoDialogFragment : DialogFragment() {

    private val viewModel: AddEstateViewModel by sharedViewModel()

    private lateinit var binding: DialogFragmentAddPhotoBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFragmentAddPhotoBinding.inflate(layoutInflater)

        isCancelable = false

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle(R.string.edit_estate_image_title)
            .setCancelable(false)
            .setPositiveButton(R.string.done) { _, _ -> }
            .setNeutralButton(R.string.cancel) { _, _ -> }
            .setNegativeButton(R.string.delete) { _, _ -> }
            .create()
    }

    override fun onResume() {
        super.onResume()

        viewModel.editingImage.observe(requireActivity()) {

            if (it == null) {
                dismiss()
            }

            binding.estateImage = it
        }

        (requireDialog() as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener {
                val description = binding.inputImageDescription.editText!!.text.toString()
                if (description.isNotEmpty()) {
                    viewModel.onEditPhoto(description)
                } else {
                    binding.inputImageDescription.error =
                        getString(R.string.add_photo_empty_description_error_text)
                }
            }

        (requireDialog() as AlertDialog).getButton(AlertDialog.BUTTON_NEUTRAL)
            .setOnClickListener { viewModel.onCancelEditing() }

        (requireDialog() as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE)
            .setOnClickListener {
                viewModel.onDeletePhoto()
            }

    }

}