package com.mrpwr.marvelismo.API

import java.lang.Exception
import java.lang.StringBuilder
import java.security.MessageDigest

class MD5Hash {

    val tsLong = System.currentTimeMillis() / 1000
    val apikey = "7b286f79dfd7a9a6b70be4fbfaa3740e"
    val pk = "e702d69c0be04abb7dfd87d58b6ad81ece44e25f"
    val ts = tsLong.toString()
    val toHash: String = ts + pk + apikey

    val hash = MD5HASH()


    private fun MD5HASH(): String {
        var result = ""

        println(ts)
        try {
            val md5 = MessageDigest.getInstance("MD5")
            val md5HashBytes = md5.digest(toHash.toByteArray()).toTypedArray()

            result = byteArrayToHexString(md5HashBytes)
        } catch (e: Exception) {
            result = "error: ${e.message}"
        }
        return result
    }


    private fun byteArrayToHexString(array: Array<Byte>): String {
        var result = StringBuilder(array.size * 2)
        for (byte in array) {
            val toAppend = String.format("%2X", byte).replace(" ", "0")

            result.append(toAppend)
        }
        return result.toString().toLowerCase()
    }


}