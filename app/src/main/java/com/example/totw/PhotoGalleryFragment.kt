package com.example.totw

import ImagePagerAdapter
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

class PhotoGalleryFragment : Fragment() {
    private lateinit var photoPicker: ActivityResultLauncher<Intent>
    lateinit var imagePagerAdapter: ImagePagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_gallery, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uploadButton = view.findViewById<Button>(R.id.uploadButton)
        val photoViewPager = view.findViewById<ViewPager>(R.id.imageViewPager)
        val images = listOf(
            R.drawable.photo1,
            R.drawable.photo2,
            R.drawable.photo3
        )
        imagePagerAdapter = ImagePagerAdapter(requireContext(), images)
        photoViewPager.adapter = imagePagerAdapter
        photoPicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val imageUri = data.data
                    if (imageUri != null) {
                        val emailIntent = Intent(Intent.ACTION_SEND)
                        emailIntent.type = "image/*"
                        emailIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("top@western.edu"))
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "TOP Photo Gallery Submission")
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Here is my photo submission.")
                        val chooserIntent = Intent.createChooser(emailIntent, "Select email app and just hit send to submit your photo!")
                        if(emailIntent.resolveActivity(requireContext().packageManager) != null){
                            Log.v("test",imageUri.toString())
                            startActivity(chooserIntent)
                        }else{
                            Toast.makeText(requireContext(), "No email apps found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Photo picker cancelled", Toast.LENGTH_SHORT).show()
            }
        }
        uploadButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            photoPicker.launch(intent)
        }

    }
}