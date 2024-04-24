package com.example.totw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

}