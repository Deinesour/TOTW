package com.example.totw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class StaffFragment : Fragment() {
    private lateinit var webView : WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_staff, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var posts = mutableListOf<Post>()
        webView = view.findViewById<WebView>(R.id.WebViewStaff)
        webView.isVerticalScrollBarEnabled = true
        webView.isHorizontalScrollBarEnabled = false
        webView.settings.allowContentAccess = true
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true

        // for manual JSON request to Staff page.
        val url = "https://topotheworld.org/wp-json/wp/v2/pages?_embed&search='Staff'&slug='Staff'"
//        Thread {
//            makeJsonRequest(url, webView)
//        }.start()
        webView.loadUrl("https://topotheworld.org/staff/")
        super.onViewCreated(view, savedInstanceState)
    }

    private fun makeJsonRequest(url: String, webView: WebView) {
        val requestQueue = Volley.newRequestQueue(requireActivity().applicationContext)
        var posts = mutableListOf<Post>()
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                posts = parseResponse(response)
                webView.loadDataWithBaseURL(null, posts[0].content, "text/html", "UTF-8", null)
            },
            { error ->
                requireActivity().runOnUiThread {
                    // Handle error
                    Toast.makeText(requireContext(), "Error fetching JSON: ${error.message}", Toast.LENGTH_LONG).show()
                }
            })

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest)
    }
    private fun parseResponse(response: String?): MutableList<Post> {
        val posts = mutableListOf<Post>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val id = jsonObject.getInt("id")
            val content = jsonObject.getJSONObject("content").getString("rendered")
            posts.add(Post(id, "", -1, "", content, ""))
        }
        return posts
    }
}