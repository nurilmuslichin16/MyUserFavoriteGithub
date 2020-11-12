package com.example.githupappuser.helper

import android.database.Cursor
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.AVATAR
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.URL
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion._ID
import com.example.githupappuser.model.UserFavorite

object MappingHelper {

    fun mapCursorToArrayList(usersCursor: Cursor?): ArrayList<UserFavorite> {
        val userList = ArrayList<UserFavorite>()

        usersCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val url = getString(getColumnIndexOrThrow(URL))
                userList.add(UserFavorite(id, avatar, username, url))
            }
        }
        return userList
    }

    fun mapCursorToObject(notesCursor: Cursor?): UserFavorite {
        var user = UserFavorite()
        notesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(_ID))
            val avatar = getString(getColumnIndexOrThrow(AVATAR))
            val username = getString(getColumnIndexOrThrow(USERNAME))
            val url = getString(getColumnIndexOrThrow(URL))
            user = UserFavorite(id, avatar, username, url)
        }
        return user
    }
}