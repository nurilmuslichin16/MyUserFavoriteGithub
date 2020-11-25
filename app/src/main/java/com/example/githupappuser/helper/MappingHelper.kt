package com.example.githupappuser.helper

import android.database.Cursor
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.AVATAR
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.URL
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.ID
import com.example.githupappuser.model.UserFavorite

object MappingHelper {

    fun mapCursorToArrayList(usersCursor: Cursor?): ArrayList<UserFavorite> {
        val userList = ArrayList<UserFavorite>()

        usersCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(ID))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val url = getString(getColumnIndexOrThrow(URL))
                userList.add(UserFavorite(id, avatar, username, url))
            }
        }
        return userList
    }
}