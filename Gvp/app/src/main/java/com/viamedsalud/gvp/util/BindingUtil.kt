package com.viamedsalud.gvp.util

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


@BindingAdapter("imageLoad")
fun loadImage(imageView: ImageView, image: Int) {
    image.let {
        Glide.with(imageView.context)
            .load(image)
            .into(imageView)
    }
}

@BindingAdapter("viewVisible")
fun visibility(view: CardView, isVisible: Boolean) {
    view.apply {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}
