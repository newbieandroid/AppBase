package com.fuyoul.sanwenseller.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.support.annotation.ColorRes
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.DrawableRequestBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fuyoul.sanwenseller.R
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.lang.Exception


/**
 *  Auther: chen
 *  Creat at: 2017\8\14 0014
 *  Desc:
 */
object GlideUtils {

    private val ERRORIMG = R.mipmap.icon_imgloading
    private val LOADINGIMG = R.mipmap.icon_imgloading

    /**
     * 加载圆形图片,带边框
     */
    fun <T> loadCircleImg(context: Context, path: T, imageView: ImageView, isBord: Boolean, @ColorRes bordColor: Int, loadingImgRes: Int, errorImgRes: Int) {

        val builder: DrawableRequestBuilder<T> = Glide.with(context)
                .load(path)
                .thumbnail(0.1f)
                .placeholder(loadingImgRes)
                .error(errorImgRes)
                .centerCrop()
                .listener(object : RequestListener<T, GlideDrawable> {
                    override fun onException(e: Exception?, model: T?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        loadCircleImg(context, ERRORIMG, imageView, isBord, bordColor, LOADINGIMG, ERRORIMG)
                        return true
                    }

                    override fun onResourceReady(resource: GlideDrawable?, model: T?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean = false

                })



        if (isBord) {
            builder.bitmapTransform(GlideCircleTransform(context, bordColor)).into(imageView)
        } else {
            builder.bitmapTransform(GlideCircleTransform(context)).into(imageView)
        }
    }

    fun <T> loadCircleImg(context: Context, path: T, imageView: ImageView, isBord: Boolean, @ColorRes bordColor: Int) {
        loadCircleImg(context, path, imageView, isBord, bordColor, LOADINGIMG, ERRORIMG)
    }


    /**
     * 加载圆形图片,不带边框
     */
    fun <T> loadCircleImg(context: Context, path: T, imageView: ImageView, loadingImgRes: Int, errorImgRes: Int) {
        loadCircleImg(context, path, imageView, false, 0, loadingImgRes, errorImgRes)
    }

    fun <T> loadCircleImg(context: Context, path: T, imageView: ImageView) {
        loadCircleImg(context, path, imageView, false, 0, LOADINGIMG, ERRORIMG)
    }

    /**
     * 加载本地或者网络圆角图片
     */
    fun <T> loadRoundCornerImg(context: Context, path: T, imageView: ImageView) {
        loadRoundCornerImg(context, path, imageView, LOADINGIMG, ERRORIMG)
    }

    fun <T> loadRoundCornerImg(context: Context, path: T, imageView: ImageView, loadingImgRes: Int, errorImgRes: Int) {

        Glide.with(context)
                .load(path)
                .thumbnail(0.1f)
                .placeholder(loadingImgRes)
                .error(errorImgRes)
                .fitCenter()
                .bitmapTransform(RoundedCornersTransformation(context, 30, 0, RoundedCornersTransformation.CornerType.ALL))
                .listener(object : RequestListener<T, GlideDrawable> {
                    override fun onException(e: Exception?, model: T?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        loadRoundCornerImg(context, ERRORIMG, imageView)
                        return true
                    }

                    override fun onResourceReady(resource: GlideDrawable?, model: T?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean = false

                })
                .into(imageView)
    }


    /**
     * 加载本地或者网络普通图片
     */
    fun <T> loadNormalImg(context: Context, path: T, imageView: ImageView) {
        loadNormalImg(context, path, imageView, LOADINGIMG, ERRORIMG)
    }

    fun <T> loadNormalImg(context: Context, path: T, imageView: ImageView, loadingImgRes: Int, errorImgRes: Int) {


        Log.e("csl", "----------$path--------")


        Glide.with(context)
                .load(path)
                .thumbnail(0.1f)
                .placeholder(loadingImgRes)
                .error(errorImgRes)
                .centerCrop()
//                .listener(object : RequestListener<T, GlideDrawable> {
//                    override fun onException(e: Exception?, model: T?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
//                        loadNormalImg(context, ERRORIMG, imageView)
//                        return true
//                    }
//
//                    override fun onResourceReady(resource: GlideDrawable?, model: T?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean = false
//
//                })
                .into(imageView)
    }


    private class GlideCircleTransform(context: Context) : BitmapTransformation(context) {

        private var mBorderPaint: Paint? = null
        private var mBorderWidth: Float = 0.toFloat()

        constructor(context: Context, @ColorRes borderColor: Int) : this(context) {

            mBorderWidth = Resources.getSystem().displayMetrics.density

            mBorderPaint = Paint()
            mBorderPaint!!.isDither = true
            mBorderPaint!!.isAntiAlias = true
            mBorderPaint!!.color = context.resources.getColor(borderColor)
            mBorderPaint!!.style = Paint.Style.STROKE
            mBorderPaint!!.strokeWidth = mBorderWidth

        }


        override fun getId(): String = "${System.currentTimeMillis()}"

        override fun transform(pool: BitmapPool?, toTransform: Bitmap?, outWidth: Int, outHeight: Int): Bitmap = circleCrop(pool!!, toTransform)!!


        private fun circleCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
            if (source == null) return null

            val size = (Math.min(source.width, source.height) - mBorderWidth / 2).toInt()
            val x = (source.width - size) / 2
            val y = (source.height - size) / 2
            val squared = Bitmap.createBitmap(source, x, y, size, size)
            var result: Bitmap? = pool.get(size, size, Bitmap.Config.ARGB_8888)
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            }
            val canvas = Canvas(result)
            val paint = Paint()
            paint.shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.isAntiAlias = true
            val r = size / 2f
            canvas.drawCircle(r, r, r, paint)
            if (mBorderPaint != null) {
                val borderRadius = r - mBorderWidth / 2
                canvas.drawCircle(r, r, borderRadius, mBorderPaint)
            }
            return result
        }

    }
}