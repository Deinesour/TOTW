package com.example.totw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment


class events2Fragment : Fragment() {
    private lateinit var eventsWebView : WebView
    private lateinit var textviewLoading: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events2_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textviewLoading = view.findViewById(R.id.textviewLoading)
        eventsWebView = view.findViewById(R.id.eventsWebView)
        eventsWebView.loadUrl("https://events.western.edu/")
        eventsWebView.settings.javaScriptEnabled = true
        eventsWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                textviewLoading.text = ""
                super.onPageFinished(view, url)
            }
        }

    }
}