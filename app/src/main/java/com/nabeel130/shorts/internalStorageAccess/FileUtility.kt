package com.nabeel130.shorts.internalStorageAccess

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.nio.charset.Charset


class FileUtility {
    companion object {
        suspend fun loadFileFromInternalStorage(allFiles: Array<File>?): List<String> {
            return withContext(Dispatchers.IO) {
                allFiles?.filter { it.canRead() && it.canWrite() && it.name.equals("security_key.txt") }
                    ?.map {
                        it.inputStream().readBytes().decodeToString()
                    }
            } ?: listOf()
        }
    }
}