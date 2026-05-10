package com.kaushal.setu.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun View.show() { visibility = View.VISIBLE }
fun View.hide() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

fun FragmentActivity.confirmDialog(
    title: String, message: String,
    positive: String = "Yes", onConfirm: () -> Unit
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positive) { _, _ -> onConfirm() }
        .setNegativeButton("Cancel", null)
        .show()
}