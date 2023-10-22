package com.example.galleryapp.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.galleryapp.R
import com.example.galleryapp.data.InternetChecker
import com.example.galleryapp.data.di.App
import com.example.galleryapp.data.loadImageWithPicasso
import com.example.galleryapp.databinding.ActivityMainBinding
import javax.inject.Inject
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainAdapter: MainAdapter
    private lateinit var overlayView: View

    @Inject lateinit var viewModel: MainViewModel
    @Inject lateinit var internetChecker: InternetChecker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as App).appComponent.inject(this)

        setUpRecyclerView()
        fetchImageData()
        observeEvents()
        swipeRefresh()
    }

    private fun swipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchImageData()
        }
    }

    @SuppressLint("ResourceType")
    private fun setUpRecyclerView() {
        mainAdapter = MainAdapter { imageUrl ->
            val dialog = createImageAlertDialog(imageUrl)

            overlayView = layoutInflater.inflate(R.layout.overlay_view, binding.root, false)
            val rootView = findViewById<ViewGroup>(android.R.id.content)
            rootView.addView(overlayView)

            dialog.setOnDismissListener {
                rootView.removeView(overlayView)
                binding.progressBar.visibility = View.GONE
            }

            dialog.show()
        }

        binding.recyclerView.apply {
            adapter = mainAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(false)
        }
    }

    private fun createImageAlertDialog(imageUrl: String?): AlertDialog {
        val alertDialog = AlertDialog.Builder(this)
        val imageView = createImageView(imageUrl)
        alertDialog.setView(imageView)

        val dialog = alertDialog.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setImageViewClickListener(imageView, dialog)

        return dialog
    }

    private fun createImageView(imageUrl: String?): ImageView {
        binding.progressBar.visibility = View.VISIBLE

        val imageView = ImageView(this)
        imageView.loadImageWithPicasso(imageUrl, binding.progressBar)
        return imageView
    }

    private fun setImageViewClickListener(imageView: ImageView, dialog: AlertDialog) {
        imageView.setOnClickListener {
            dialog.dismiss()
        }
    }


    private fun fetchImageData() {
        if (!internetChecker.isInternetAvailable()) {
            viewModel.photoResponse.value = emptyList()
            mainAdapter.setData(emptyList())
            Toast.makeText(this, "Cannot load image", Toast.LENGTH_SHORT).show()
            return
        }
        val randomPage = Random.nextInt(1, 15)
        viewModel.getImagesFromApi(randomPage)
    }
    private fun observeEvents() {
        viewModel.errorMessage.observe(this){ errorMessage ->
            Toast.makeText(this, "$errorMessage", Toast.LENGTH_SHORT).show()
        }

        viewModel.photoResponse.observe(this){
            mainAdapter.setData(it)
            binding.swipeRefreshLayout.isRefreshing = false

        }
    }
}