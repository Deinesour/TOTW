package com.example.totw

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [newsCategoriesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
interface OnCategorySelectedListener {
    fun onCategorySelected(category: String)
}

data class Category(val name: String, val imageSrc: Int)
val categories = listOf(
    Category("All News", R.drawable.news),
    Category("Sports", R.drawable.sports),
    Category("Environment", R.drawable.environment),
    Category("Opinion", R.drawable.opinion),
    //Category("Art", R.drawable.art),
    //Category("Tips & Recipes", R.drawable.recipes)
)
open class CategoryAdapter(val categories: List<Category>, val listener: OnCategorySelectedListener) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.categoryImageView)
        val categoryButton = itemView.findViewById<Button>(R.id.categoryButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val categoryItemView = LayoutInflater.from(parent.context).inflate(R.layout.news_category_item, parent, false)
        return CategoryViewHolder(categoryItemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.imageView.setImageResource(categories[position].imageSrc)
        holder.categoryButton.text = categories[position].name

        holder.categoryButton.setOnClickListener {
            listener.onCategorySelected(categories[position].name)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}
class newsCategoriesFragment : Fragment(), OnCategorySelectedListener {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_categories_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.categoriesRecyclerView)
        val adapter = CategoryAdapter(categories, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
    }

    override fun onCategorySelected(category: String) {
        val homeFrag = homeFragment()
        val bundle = Bundle().apply {
            putString("category", category)
        }
        //bundle.putString("category", category)
        homeFrag.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragment, homeFrag)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment newsCategories_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            newsCategoriesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}