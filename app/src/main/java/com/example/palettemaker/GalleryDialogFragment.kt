package com.example.palettemaker

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment

class GalleryDialogFragment: DialogFragment() {
    internal lateinit var listener: GalleryDialogListener

    interface GalleryDialogListener {
        fun onDialogGalleryClick(dialog: DialogFragment)
        fun onDialogLastImageClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(ContextThemeWrapper(context, android.R.style.DeviceDefault_Light_ButtonBar_AlertDialog))
            builder.setMessage(R.string.gallery_dialog_text)
                .setPositiveButton(R.string.gallery_label
                ) { _, _ ->
                    // Send the positive button event back to the host activity
                    listener.onDialogGalleryClick(this)
                }

                .setNegativeButton(R.string.last_image_label
                ) { _, _ ->
                    listener.onDialogLastImageClick(this)
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as GalleryDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement GalleryDialogListener"))
        }
    }

    override fun onStart() {
        super.onStart()
        val positive = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
        positive.setTextColor(Color.RED)
        positive.setBackgroundColor(Color.TRANSPARENT)
        val negative = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE)
        negative.setTextColor(Color.DKGRAY)
        negative.setBackgroundColor(Color.TRANSPARENT)
    }

}