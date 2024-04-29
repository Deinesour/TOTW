package com.totw.totw

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.jsoup.Jsoup

data class Post(
    val id: Int,
    val title: String,
    val author: Int,
    val date: String,
    val content: String,
    val mainImage: String,
    val link: String
)
interface OnArticleSelectedListener {
    fun onArticleSelected(id: Int, title: String, date: String, content: String, link: String)
}
open class ArticleAdapter(val posts: MutableList<Post>, val listener: OnArticleSelectedListener) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val buttonTitle = itemView.findViewById<Button>(R.id.buttonTitle)
        val itemImageView = itemView.findViewById<ImageView>(R.id.itemImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val categoryItemView = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return ArticleViewHolder(categoryItemView)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        Picasso.get().load(posts[position].mainImage).error(R.drawable.news).resize(1000, 800).centerInside().into(holder.itemImageView)
        holder.buttonTitle.text = posts[position].title
        holder.buttonTitle.setOnClickListener {
            listener.onArticleSelected(posts[position].id, posts[position].title, posts[position].date, posts[position].content, posts[position].link)
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
    private lateinit var textViewLoading: TextView
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPrefGen: SharedPreferences
    private lateinit var sharedPrefEnviron: SharedPreferences
    private lateinit var sharedPrefSports: SharedPreferences
    private lateinit var sharedPrefOpinion: SharedPreferences
    private var isGeneral =  false
    var isOpinion = false
    var isSports = false
    var isEnviron = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        sharedPrefGen = requireContext().getSharedPreferences("generalSwitchState", Context.MODE_PRIVATE)
        sharedPrefOpinion = requireContext().getSharedPreferences("opinionSwitchState", Context.MODE_PRIVATE)
        sharedPrefEnviron = requireContext().getSharedPreferences("environSwitchState", Context.MODE_PRIVATE)
        sharedPrefSports = requireContext().getSharedPreferences("sportsSwitchState", Context.MODE_PRIVATE)

        // Observe switch states
        //sharedViewModel.switchStates.observe(viewLifecycleOwner, Observer { switchStates ->
            //val switchStates = sharedViewModel.switchStates.value
        isGeneral = sharedPrefGen.getBoolean("generalSwitchState", false)
        isOpinion = sharedPrefOpinion.getBoolean("opinionSwitchState", false)
        isSports = sharedPrefSports.getBoolean("sportsSwitchState", false)
        isEnviron = sharedPrefEnviron.getBoolean("environSwitchState", false)
        //})

        // URL to fetch JSON from
        // Category codes for URL:
        // 8 = Environment, 7 = Opinion, 6 = Sports, 5 = ???
        var url = "https://topotheworld.org/wp-json/wp/v2/posts?_embed&per_page=100"

        textViewHeader = view.findViewById(R.id.textViewHeader)
        recyclerView = view.findViewById(R.id.recyclerViewArticles)
        requestQueue = Volley.newRequestQueue(requireActivity().applicationContext)
        textViewLoading = view.findViewById(R.id.TextviewLoading)

        // check for article preferences set
        val prefList = mutableListOf<Int>()
        if (isOpinion){
            prefList.add(7)
        }
        if (isSports) {
            prefList.add(6)
        }
        if (isEnviron) {
            prefList.add(8)
        }
        if (isGeneral) {
            prefList.clear()
        }
        if (prefList.isNotEmpty()) {
            url += "&categories="
            for (pref in prefList) {
                url += "$pref,"
            }
        }

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
        textViewLoading.text = "Loading..."
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
                    textViewLoading.text = ""
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
            var title = jsonObject.getJSONObject("title").getString("rendered")
            title = title.replace("&#8217;", "'")
            val content = jsonObject.getJSONObject("content").getString("rendered")
            val author = jsonObject.getInt("author")
            val date = jsonObject.getString("date")
            val link = jsonObject.getString("link")


            val doc = Jsoup.parse(content)
            doc.select("html").forEach {
                it.attr("style", "@import url('https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap'); font-family: \"Noto Sans\", sans-serif; font-weight: 500; font-style: normal;")
            }
            doc.select("figure").forEach {
                it.attr("style", "width:96vw; height:fit-content; padding:0; margin:0; object-fit: cover; overflow: hidden;")
            }
            doc.select("img").forEach { // Checking all images
                it.attr("height", "fit-content")
                it.attr("width", "100%") // If not width is set
                it.attr("style", "vertical-align: center;padding-bottom: 0; padding-right: 10px; justify-content: center; display: block;margin-top: -175px;")
            }
            doc.select("p").forEach {
                it.attr("style", "line-height: 30px; font-size: 14pt; font-style: normal;")
            }
            doc.select("iframe").forEach { // Checking all videos
                it.attr("width", "100%") // If not width is set
            }
            val regex = Regex("data-src=\"([^\"]+)\"")
            val matchResult = regex.find(content)

            val url = matchResult?.groups?.get(1)?.value
            if (url != null) {
                val mainImage = url
                posts.add(Post(id, title, author, date, "<head></head>$doc" /*doc.toString()*/,mainImage, link))
            } else {
                val mainImage = "https://topotheworld.org/wp-content/uploads/2023/10/4ifi6ybkbvn01-400x250.jpg"
                posts.add(Post(id, title, author, date, "<head></head>$doc",mainImage, link))
            }
        }
        return posts
    }

    override fun onArticleSelected(id: Int, title: String, date: String, content: String, link: String) {
        val articleFrag = ArticleFragment()
        val bundle = Bundle().apply {
            putInt("id", id)
            putString("content", content)
            putString("title", title)
            putString("date", date)
            putString("link", link)
        }
        //bundle.putString("category", category)
        articleFrag.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragment, articleFrag)
            .addToBackStack(null)
            .commit()
    }
}
