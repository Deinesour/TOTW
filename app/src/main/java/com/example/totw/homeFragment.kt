package com.example.totw

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 * Use the [homeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

data class Article (
    val id: Int,
    val title: String,
    val content: String
)
class homeFragment : Fragment() {

    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestQueue = Volley.newRequestQueue(requireContext())
    }

    private fun makeGetRequest(url: String): String {
        var title = ""
        var rendered = ""

        class Thread(name: String?, private val handler: Handler) :
            HandlerThread(name) {
            override fun run() {

                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    { response ->
                        // Handle the response
                        var jsonObject: JSONObject? = null
                        jsonObject = JSONObject(response)
                        val content = jsonObject.getJSONObject("content")
                        rendered = content.getString("rendered")
                        Toast.makeText(context, rendered, Toast.LENGTH_LONG).show()
                    },
                    { error ->
                        // Handle errors
                        error.printStackTrace()
                    })
                requestQueue.add(stringRequest)
                val message = Message.obtain()
                message.what = 1
                handler.sendMessage(message)
            }
        }

        return title
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
        val title = makeGetRequest(url)

        textView.text = title

        webView.loadUrl(url)
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