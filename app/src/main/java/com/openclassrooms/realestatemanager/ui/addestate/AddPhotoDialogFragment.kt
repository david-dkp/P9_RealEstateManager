package com.openclassrooms.realestatemanager.ui.addestate

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.DialogFragmentAddPhotoBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddPhotoDialogFragment : DialogFragment() {

    private val viewModel: AddEstateViewModel by sharedViewModel()

    private lateinit var binding: DialogFragmentAddPhotoBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {



        return TODO();
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
    }

}