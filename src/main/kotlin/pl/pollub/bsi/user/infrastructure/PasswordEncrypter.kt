package pl.pollub.bsi.user.infrastructure

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

internal class PasswordEncrypter {
    companion object {
        @JvmStatic
        fun encrypt(algorithm: String, password: String, key: String?): String {
            return when (algorithm) {
                "SHA512" -> SHA512.encrypt(password, key)
                "HMAC" -> HMAC.encrypt(password)
                else -> AES.encrypt(password, key)
            }
        }
    }

    class SHA512 {
        companion object {
            private const val pepper = "a5354ff8"
            fun encrypt(password: String, salt: String?): String {
                val messageDigest: MessageDigest = MessageDigest.getInstance("SHA-512")
                messageDigest.update((password + salt + pepper).toByteArray(StandardCharsets.UTF_8))
                var hashText = BigInteger(1, messageDigest.digest()).toString(16)
                while (hashText.length < 32) {
                    hashText += '0'
                }
                return hashText
            }
        }
    }

    class HMAC {
        companion object {
            private const val key = "47733c1cc9694d81"
            fun encrypt(password: String): String {
                val byteKey = key.toByteArray(StandardCharsets.UTF_8)
                val sha512Hmac = Mac.getInstance("HmacSHA512")
                val keySpec = SecretKeySpec(byteKey, "HmacSHA512")
                sha512Hmac.init(keySpec)
                val macdata = sha512Hmac.doFinal(password.toByteArray(StandardCharsets.UTF_8))
                return Base64.getEncoder().encodeToString(macdata)
            }
        }
    }

    class AES {
        companion object {
            fun encrypt(password: String, key: String?): String {
                val cipher = Cipher.getInstance("AES")
                cipher.init(Cipher.ENCRYPT_MODE, transformKey(key))
                val encrypted = cipher.doFinal(password.toByteArray())
                return Base64.getEncoder().encodeToString(encrypted)
            }

            @JvmStatic
            fun decrypt(password: String, key: String): String {
                val cipher = Cipher.getInstance("AES")
                cipher.init(Cipher.DECRYPT_MODE, transformKey(key))
                val decoded = Base64.getDecoder().decode(password)
                return String(cipher.doFinal(decoded))
            }

            private fun transformKey(key: String?): Key {
                val messageDigest = MessageDigest.getInstance("MD5")
                val digest = messageDigest.digest(key?.toByteArray())
                return SecretKeySpec(digest, "AES")
            }
        }
    }
}
