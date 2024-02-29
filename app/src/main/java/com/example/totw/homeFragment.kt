package com.example.totw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.json.JSONException
import org.json.JSONObject


data class Article (
    val id: Int,
    val title: String,
    val content: String
)

class homeFragment : Fragment() {
    private lateinit var requestQueue: RequestQueue
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = "https://topotheworld.org/wp-json/wp/v2/posts?per_page=1"
        val webView: WebView = view.findViewById(R.id.webViewHome)
        val textView: TextView = view.findViewById(R.id.textView)
        //webView.loadUrl(url)
        requestQueue = Volley.newRequestQueue(requireActivity().applicationContext)

        // make a JSON request on a background thread

        Thread {
            makeJsonRequest(url, webView, textView)
        }.start()

    }
    private fun makeJsonRequest(url: String, webView: WebView, textView: TextView) {
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                // Handle the response on the main/UI thread if needed

                //val title = response.getJSONObject("title")

                //val content = jsonObject.getJSONObject("content")
                //val rendered = content.getString("rendered")
                requireActivity().runOnUiThread {
                    // Process jsonResponse
                    Toast.makeText(requireContext(), "In function", Toast.LENGTH_LONG).show()
                    //view?.findViewById<WebView>(R.id.webViewHome)
                        //?.loadData(rendered, "text/html", null)
                    textView.text = response
                }
            },
            { error ->
                // Handle error on the main/UI thread if needed
                requireActivity().runOnUiThread {
                    // Handle error
                    error.printStackTrace()
                    Toast.makeText(requireContext(), "Error fetching JSON.", Toast.LENGTH_LONG).show()
                }
            })

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest)
    }
}


class NotificationsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationsFragment().apply {

            }
    }
}