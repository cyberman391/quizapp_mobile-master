package com.quizest.quizestapp.UtilPackge

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class KotlinUtil {

    companion object {
        @TargetApi(Build.VERSION_CODES.P)
        fun getAndroidHashKey(context: Context) {
            try {
                val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName,
                        PackageManager.GET_SIGNING_CERTIFICATES
                )
                var signatures = arrayOf<Signature>()
                signatures = packageInfo.signingInfo.apkContentsSigners
                for (signature in signatures) {
                    val messageDigest = MessageDigest.getInstance("SHA")
                    messageDigest.update(signature.toByteArray())
                    val hashKey = String(Base64.encode(messageDigest.digest(), 0))
                    Log.i("Hash Key", hashKey)
                }
            } catch (e: NoSuchAlgorithmException) {
                Log.e("Algorith", e.message)
            } catch (e: Exception) {
                Log.e("exceiption", e.message)
            }
        }
    }

}