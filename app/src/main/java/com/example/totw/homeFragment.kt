package com.example.totw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import kotlin.reflect.KFunction3

/**
 * A simple [Fragment] subclass.
 * Use the [homeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
data class Post(
    val id: Int,
    val title: String,
    val author: Int,
    val date: String,
    val content: String
)
interface OnArticleSelectedListener {
    fun onArticleSelected(id: Int, title: String, content: String)
}
open class ArticleAdapter(val posts: MutableList<Post>, val listener: OnArticleSelectedListener) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val buttonTitle = itemView.findViewById<Button>(R.id.buttonTitle)
        val textViewDate = itemView.findViewById<TextView>(R.id.textViewDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val categoryItemView = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return ArticleViewHolder(categoryItemView)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.textViewDate.text = posts[position].date
        holder.buttonTitle.text = posts[position].title
        holder.buttonTitle.setOnClickListener {
            listener.onArticleSelected(posts[position].id, posts[position].title, posts[position].content)
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
class homeFragment : Fragment(), OnArticleSelectedListener {
    private lateinit var requestQueue: RequestQueue
    private lateinit var textViewHeader: TextView
    private lateinit var recyclerView: RecyclerView

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
        // 8 = environ, 7 = Opinion, 6 = Sports, 5 = ???
        var url = "https://topotheworld.org/wp-json/wp/v2/posts?_embed"

        textViewHeader = view.findViewById(R.id.textViewHeader)
        recyclerView = view.findViewById(R.id.recyclerViewArticles)
        requestQueue = Volley.newRequestQueue(requireActivity().applicationContext)

        if (arguments?.getString("category") != null) {
            val category = arguments?.getString("category")
            textViewHeader.text = category
            when (category) {
                "Sports" -> url += "&categories=6"
                "Opinion" -> url += "&categories=7"
                "Environment" -> url += "&categories=8"
            }

        }

        // make a JSON request on a background thread - required by Android
        //textViewTitle.text = "Loading Content..."

        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        Thread {
            val posts = makeJsonRequest(url, recyclerView)
        }.start()

    }
    private fun makeJsonRequest(url: String, recyclerView: RecyclerView): MutableList<Post> {
        var posts =  mutableListOf<Post>()
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                posts = parseResponse(response)
                requireActivity().runOnUiThread {
                    // Process jsonResponse on Main UI thread
                    //webView.loadData(posts[0].content, "text/html", null)
                    val adapter = ArticleAdapter(posts, this)
                    recyclerView.adapter = adapter
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
        return posts
    }

    private fun parseResponse(response: String): MutableList<Post> {
        val posts = mutableListOf<Post>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val id = jsonObject.getInt("id")
            val title = jsonObject.getJSONObject("title").getString("rendered")
            val content = jsonObject.getJSONObject("content").getString("rendered")
            val author = jsonObject.getInt("author")
            val date = jsonObject.getString("date")
            posts.add(Post(id, title, author, date, content))
        }
        return posts
    }

    override fun onArticleSelected(id: Int, title: String, content: String) {
        val articleFrag = ArticleFragment()
        val bundle = Bundle().apply {
            putInt("id", id)
            putString("content", content)
            putString("title", title)
        }
        //bundle.putString("category", category)
        articleFrag.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragment, articleFrag)
            .addToBackStack(null)
            .commit()
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