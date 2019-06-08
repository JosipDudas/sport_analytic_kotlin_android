package com.dudas.sportanalytic.utils

import timber.log.Timber
import java.lang.Exception
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

const val SHA_256 = "SHA-256"
const val AES = "AES"
const val ALGORITHM = "AES/CBC/PKCS5Padding"
const val HEX_CHARS = "0123456789ABCDEF"

fun generateHashOfPassword(password: String): String {
    val bytes = MessageDigest
        .getInstance(SHA_256)
        .digest(password.toByteArray())
    val result = StringBuilder(bytes.size * 2)

    bytes.forEach {
        val i = it.toInt()
        result.append(HEX_CHARS[i shr 4 and 0x0f])
        result.append(HEX_CHARS[i and 0x0f])
    }

    return result.toString()
}

fun bin2hex(data: ByteArray): String {
    return String.format("%0" + data.size * 2 + "X", BigInteger(1, data))
}

private fun generateKey(password: String): SecretKeySpec {
    val digest = MessageDigest.getInstance(SHA_256)
    val bytes = password.toByteArray(Charsets.UTF_8)
    digest.update(bytes, 0, bytes.size)
    val key = digest.digest()
    return SecretKeySpec(key, AES)
}

fun encrypt(data: String, password: String): String {
    val cipher = Cipher.getInstance(ALGORITHM)
    val iv = ByteArray(16)
    Arrays.fill(iv, 0x00.toByte())
    val ivParameterSpec = IvParameterSpec(iv)
    cipher.init(Cipher.ENCRYPT_MODE, generateKey(password), ivParameterSpec)
    val encVal = cipher.doFinal(data.toByteArray())
    val encryptData = String(android.util.Base64.encode(encVal, android.util.Base64.DEFAULT))
    return encryptData.trim()
}

fun decrypt(data: String, password: String): String {
    val cipher = Cipher.getInstance(ALGORITHM)
    val iv = ByteArray(16)
    Arrays.fill(iv, 0x00.toByte())
    val ivParameterSpec = IvParameterSpec(iv)
    cipher.init(Cipher.DECRYPT_MODE, generateKey(password), ivParameterSpec)
    val decodeValue = android.util.Base64.decode(data, android.util.Base64.DEFAULT)
    val decValue = cipher.doFinal(decodeValue)
    val decryptData = String(decValue)
    return decryptData.trim()
}