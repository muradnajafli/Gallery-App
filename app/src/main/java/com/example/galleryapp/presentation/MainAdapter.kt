package com.example.galleryapp.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.galleryapp.data.loadImageWithGlide
import com.example.galleryapp.data.model.PhotoItem
import com.example.galleryapp.databinding.RecyclerviewItemBinding

class MainAdapter(private val onImageClickListener: (String?) -> Unit): RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var imageList: List<PhotoItem> = emptyList()

    inner class MainViewHolder(val binding: RecyclerviewItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val imageItem = imageList[position]
        val imageHolder = holder.binding
        imageHolder.imageView.loadImageWithGlide(imageItem.downloadUrl, imageHolder.progressBar)

        holder.binding.imageView.setOnClickListener {
            onImageClickListener(imageItem.downloadUrl)
        }
    }

    override fun getItemCount() = imageList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<PhotoItem>) {
        imageList = newList

        notifyDataSetChanged()
    }
}