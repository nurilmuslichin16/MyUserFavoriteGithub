package com.example.githupappuser.widget

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.example.githupappuser.R
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githupappuser.helper.MappingHelper
import com.example.githupappuser.model.UserFavorite

internal class StackRemoteViewsFactory(private val mContext: Context): RemoteViewsService.RemoteViewsFactory {

    private var user = ArrayList<UserFavorite>()
    private var mItemWidget = ArrayList<String>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val cursor = mContext.contentResolver?.query(CONTENT_URI, null, null, null, null)
        if (cursor != null) {
            cursor.close()
        }
        user = MappingHelper.mapCursorToArrayList(cursor)

        val identityToken = Binder.clearCallingIdentity()

        for (item in user) {
            mItemWidget.add(item.username.toString())
        }

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = user.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setTextViewText(R.id.widget_txt_name, mItemWidget[position])

        val extras = bundleOf(
            GithubAppWidget.EXTRA_ITEM to position
        )
        val fillIntent = Intent()
        fillIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.widget_txt_name, fillIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}