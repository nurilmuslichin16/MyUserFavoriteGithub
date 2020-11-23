package com.example.githupappuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githupappuser.db.DatabaseContract.AUTHORITY
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.example.githupappuser.db.UserFavoriteHelper

class UserFavoriteProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        userFavoriteHelper = UserFavoriteHelper.getInstance(context as Context)
        userFavoriteHelper.open()
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val cursor: Cursor?
        when(sUriMatcher.match(uri)) {
            USER -> cursor = userFavoriteHelper.queryAll()
            USER_ID -> cursor = userFavoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> cursor = null
        }

        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> userFavoriteHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        val updated: Int = when(USER_ID) {
            sUriMatcher.match(uri) -> userFavoriteHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> userFavoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }

    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userFavoriteHelper: UserFavoriteHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)

            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)
        }
    }
}