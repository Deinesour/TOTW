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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

/**
 * A simple [Fragment] subclass.
 * Use the [homeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
data class Post(
    val id: Int,
    val title: String,
    val content: String
)

class homeFragment : Fragment() {
    private lateinit var requestQueue: RequestQueue
    private lateinit var webView: WebView
    private lateinit var textView: TextView

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

        // URL to fetch JSON from
        val url = "https://topotheworld.org/wp-json/wp/v2/posts?fields=id,title,content"

        webView = view.findViewById(R.id.webViewHome)
        textView = view.findViewById(R.id.textViewHomeTitle)
        requestQueue = Volley.newRequestQueue(requireActivity().applicationContext)

        // make a JSON request on a background thread - required by Android
        textView.text = "Loading Content..."
        Thread {
            makeJsonRequest(url, webView, textView)
        }.start()

    }
    private fun makeJsonRequest(url: String, webView: WebView, textView: TextView) {
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                val posts = parseResponse(response)
                requireActivity().runOnUiThread {
                    // Process jsonResponse on Main UI thread
                    webView.loadData(posts[0].content, "text/html", null)
                    textView.text = posts[0].title
                }
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

    private fun parseResponse(response: String): List<Post> {
        val posts = mutableListOf<Post>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val id = jsonObject.getInt("id")
            val title = jsonObject.getJSONObject("title").getString("rendered")
            val content = jsonObject.getJSONObject("content").getString("rendered")
            posts.add(Post(id, title, content))
        }
        return posts
    }
}

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationsFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotificationsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationsFragment().apply {

            }
    }
}