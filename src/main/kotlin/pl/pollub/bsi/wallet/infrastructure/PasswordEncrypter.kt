package pl.pollub.bsi.wallet.infrastructure

import java.security.Key
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

internal class PasswordEncrypter {
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
