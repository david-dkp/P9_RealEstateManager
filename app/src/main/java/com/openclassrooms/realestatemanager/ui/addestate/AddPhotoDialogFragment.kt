package com.openclassrooms.realestatemanager.ui.addestate

import android.animation.Animator
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.DialogFragmentAddPhotoBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddPhotoDialogFragment : DialogFragment() {

    private val viewModel: AddEstateViewModel by sharedViewModel()

    private lateinit var binding: DialogFragmentAddPhotoBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogFragmentAddPhotoBinding.inflate(layoutInflater)

        viewModel.editingImage.observe(viewLifecycleOwner) {

            if (it == null) {
                dismiss()
            }

            binding.estateImage = it
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle(R.string.edit_estate_image_title)
            .setPositiveButton(R.string.done) { dialog, which ->
                val description = binding.inputImageDescription.editText!!.text.toString()
                if (description.isNotEmpty()) {
                    view?.let {
                        it
                            .animate()
                            .translationX(0f)
                            .setDuration(500)
                            .withEndAction {
                                viewModel.onEditPhoto(description)
                            }
                    }
                } else {
                    binding.inputImageDescription.error = getString(R.string.add_photo_empty_description_error_text)
                }
            }
            .setNeutralButton(R.string.cancel) { dialog, which ->
                viewModel.onCancelEditing()
            }
            .setNegativeButton(R.string.delete) { dialog, which ->
                viewModel.onDeletePhoto()
            }
            .create()
    }

}