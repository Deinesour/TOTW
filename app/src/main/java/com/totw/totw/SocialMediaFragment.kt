package com.totw.totw

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class SocialMediaFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_social_media, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val xTextView: TextView = view.findViewById(R.id.xTextview)
        val tiktokTextview: TextView = view.findViewById(R.id.tiktokTextView)
        val instaTextView: TextView = view.findViewById(R.id.instaTextview)

        instaTextView.setOnClickListener {
            val url = "https://www.instagram.com/topotheworldnews?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw=="
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)

            startActivity(intent)
        }
        tiktokTextview.setOnClickListener{
            val url = "https://www.tiktok.com/en/"
            val intent = Intent(Intent.ACTION_VIEW,Uri.parse(url))
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        xTextView.setOnClickListener{
            val url = "https://twitter.com/toplocalnews?lang=en"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }




}