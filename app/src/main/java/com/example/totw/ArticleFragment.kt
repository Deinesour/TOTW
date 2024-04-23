package com.example.totw

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.totw.R.id.webView
import java.io.InputStream


class ArticleFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var id = 0
        var title = "Title"
        var content = "<html></html>"

        if (arguments?.getString("title") != null) {
            id = arguments?.getInt("id")!!
            title = arguments?.getString("title")!!
            content = arguments?.getString("content")!!
        }

        val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
        val webView = view.findViewById<WebView>(webView)

        textViewTitle.text = title
        // IMAGES DON'T DISPLAY WITH JS ENABLED
        //webView.settings.javaScriptEnabled = true
        webView.isVerticalScrollBarEnabled = true
        webView.isHorizontalScrollBarEnabled = false
        webView.settings.allowContentAccess = true
        webView.settings.domStorageEnabled = true
        //webView.settings.loadWithOverviewMode = true
        //webView.settings.builtInZoomControls = true
        webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)

        webView.setOnTouchListener(object : OnTouchListener {
            var m_downX = 0f
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (event.pointerCount > 1) {
                    //Multi touch detected
                    return true
                }
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // save the x
                        m_downX = event.x
                    }
                    MotionEvent.ACTION_MOVE, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {

                        // set x so that it doesn't move
                        event.setLocation(m_downX, event.y)
                    }
                }
                return false
            }
        })
    }
}