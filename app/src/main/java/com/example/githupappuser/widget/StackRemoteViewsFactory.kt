package com.example.githupappuser.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.githupappuser.R
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githupappuser.helper.MappingHelper
import com.example.githupappuser.model.UserFavorite
import kotlinx.android.synthetic.main.item_user_favorite.view.*

internal class StackRemoteViewsFactory(private val mContext: Context): RemoteViewsService.RemoteViewsFactory {

    private var user = ArrayList<UserFavorite>()
    private var mItemWidget = ArrayList<Bitmap>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()

        val cursor = mContext.contentResolver?.query(CONTENT_URI, null, null, null, null)

        if (cursor != null) {
            cursor.close()
        }

        user = MappingHelper.mapCursorToArrayList(cursor)

        for (item in user) {
            val avatar = Glide.with(mContext)
                .asBitmap()
                .load(item.avatar)
                .placeholder(R.drawable.github)
                .error(R.drawable.github)
                .submit(512, 512)
                .get()
            mItemWidget.add(avatar)
        }

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = user.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.widget_img_avatar, mItemWidget[position])

        val extras = bundleOf(
            GithubAppWidget.EXTRA_ITEM to position
        )
        val fillIntent = Intent()
        fillIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.widget_img_avatar, fillIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}