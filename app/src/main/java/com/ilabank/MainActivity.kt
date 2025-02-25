package com.ilabank

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.ilabank.adapter.ImageAdapter
import com.ilabank.adapter.ItemAdapter
import com.ilabank.databinding.ActivityMainBinding
import com.ilabank.model.Item

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: ItemAdapter

    private lateinit var imageList: List<Int>  // List of images (as drawable resources)
    private lateinit var dots: ArrayList<View>  // List of dot indicators

    private var itemList = listOf(
        Item(R.drawable.bank1, "Bank1"),
        Item(R.drawable.bank2, "HDFC"),
        Item(R.drawable.bank3, "ICICI"),
        Item(R.drawable.bank4, "Bank2"),
        Item(R.drawable.bank5, "HDFC"),
        Item(R.drawable.bank1, "SBI"),
        // Add more items as needed
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        setListItems()
        loadImages()

    }

    private fun setListItems() {
        // Set up RecyclerView
        adapter = ItemAdapter(itemList) { item ->
            Toast.makeText(this, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Add a divider between items
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)


        // Set up search functionality
        binding.search.addTextChangedListener { editable ->
            val query = editable.toString()
            val filteredList = itemList.filter { it.text.contains(query, ignoreCase = true) }
            adapter.filterList(filteredList)
        }
    }

    private fun loadImages() {
        // Sample images (use image resources)
        imageList = listOf(
            R.drawable.image_bank,
            R.drawable.bank,
            R.drawable.image1,
            R.drawable.image,
        )

        val adapter = ImageAdapter(imageList)
        binding.imageCarousel.adapter = adapter

        // Create the custom dot indicators
        createDotIndicators()

        // Register a page change callback to update dot indicators when the page changes
        binding.imageCarousel.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDotIndicators(position)
            }
        })

    }

    // Create dot indicators dynamically based on the number of images
    private fun createDotIndicators() {
        dots = ArrayList()

        // Create a dot for each image in the imageList
        for (i in imageList.indices) {
            val dot = View(this)
            val params = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.dot_size),
                resources.getDimensionPixelSize(R.dimen.dot_size)
            )
            params.setMargins(8, 0, 8, 0)
            dot.layoutParams = params
            dot.setBackgroundResource(R.drawable.dot_inactive) // Set inactive dot background
            binding.dotIndicators.addView(dot)
            dots.add(dot)
        }

        // Set the first dot as active initially
        updateDotIndicators(0)
    }


    // Update dot indicators based on the current page position
    private fun updateDotIndicators(position: Int) {
        for (i in dots.indices) {
            if (i == position) {
                dots[i].setBackgroundResource(R.drawable.dot_active)  // Active dot
            } else {
                dots[i].setBackgroundResource(R.drawable.dot_inactive)  // Inactive dot
            }
        }
    }


}

