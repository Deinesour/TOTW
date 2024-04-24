package com.example.totw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment


class newsletterFragment : Fragment() {
    private lateinit var newsletterWebView : WebView
    private lateinit var textviewLoading: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_newsletter_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textviewLoading = view.findViewById(R.id.textviewLoading)
        newsletterWebView = view.findViewById(R.id.newsletterWebView)
        newsletterWebView.loadUrl("https://topotheworld.us20.list-manage.com/subscribe?u=db7490384e9fdb64cd1c56846&id=9087e84b2a")
        newsletterWebView.settings.javaScriptEnabled = true
        newsletterWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                textviewLoading.text = ""
                super.onPageFinished(view, url)
            }
        }

    }
}