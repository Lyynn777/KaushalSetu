package com.kaushal.setu.ui.worker

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kaushal.setu.R
import com.kaushal.setu.databinding.ActivityProfileCardBinding
import com.kaushal.setu.viewmodel.WorkerViewModel
import java.io.File
import java.io.FileOutputStream
import com.kaushal.setu.ui.common.BaseActivity
class ProfileCardActivity : BaseActivity() {
    private lateinit var b: ActivityProfileCardBinding
    private val vm: WorkerViewModel by viewModels()
    private val uid get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfileCardBinding.inflate(layoutInflater)
        setContentView(b.root)
        vm.loadProfile(uid)

        vm.profile.observe(this) { p ->
            p ?: return@observe
            b.cardName.text = p.name
            b.cardSkill.text = p.skillCategory
            b.cardLocation.text = "📍 ${p.location}"
            b.cardExp.text = getString(R.string.years_exp, p.yearsOfExperience)
            b.cardRating.text = "★ %.1f  (${p.totalRatings} reviews)".format(p.averageRating)
            b.cardPhone.text = "📞 ${p.phone}"
            b.cardAvailability.text = if (p.available) getString(R.string.status_available) else getString(R.string.status_busy)
            b.cardAvailability.setBackgroundResource(if (p.available) R.drawable.bg_available else R.drawable.bg_busy)
            if (p.profileImageUrl.isNotEmpty())
                Glide.with(this).load(p.profileImageUrl).circleCrop().into(b.cardAvatar)
        }

        b.btnBack.setOnClickListener { finish() }
        b.btnShareWhatsApp.setOnClickListener { share("com.whatsapp") }
        b.btnShareOther.setOnClickListener    { share(null) }
        b.btnSaveGallery.setOnClickListener   { saveToGallery() }
    }

    private fun cardBitmap(): Bitmap {
        val v = b.profileCardView
        val bmp = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        v.draw(Canvas(bmp)); return bmp
    }

    private fun cacheUri() = try {
        val dir = File(cacheDir, "cards").also { it.mkdirs() }
        val f = File(dir, "ks_card.png")
        FileOutputStream(f).use { cardBitmap().compress(Bitmap.CompressFormat.PNG, 100, it) }
        FileProvider.getUriForFile(this, "${packageName}.fileprovider", f)
    } catch (e: Exception) { null }

    private fun share(pkg: String?) {
        val uri = cacheUri() ?: return
        val i = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, "${b.cardName.text} — ${b.cardSkill.text}\n📞 ${b.cardPhone.text}\nFind me on KaushalSetu")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pkg?.let { setPackage(it) }
        }
        startActivity(Intent.createChooser(i, "Share Profile Card"))
    }

    private fun saveToGallery() {
        try {
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val f = File(dir, "ks_card_${System.currentTimeMillis()}.png")
            FileOutputStream(f).use { cardBitmap().compress(Bitmap.CompressFormat.PNG, 100, it) }
            Toast.makeText(this, getString(R.string.card_saved), Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}
