package server

import java.io.File
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class Response(private val stream: OutputStream, private val status: Status) {

    fun send() {
        stream.write(createRes(baseRes()))
    }

    fun send(file: File, includeContent: Boolean) {
        val res = baseRes()
        res.append("Content-Length: ${file.length()}\r\n")
        getContentType(file.extension)?.let { res.append("Content-Type: $it\r\n\r\n") }
        stream.write(createRes(res))
        if (includeContent) {
            file.inputStream().use { it.copyTo(stream) }
        }
    }

    private fun createRes(res: StringBuilder): ByteArray {
        return res.toString().toByteArray()
    }

    private fun baseRes(): StringBuilder {
        val dateFormat = SimpleDateFormat("EEE, dd MMM, yyyy HH:mm:ss z", Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        val result = StringBuilder()
        result.append("HTTP/1.1 ${status.code} ${status.value}\r\n")
                .append("Date: ${dateFormat.format(Date())}\r\n")
                .append("Server: SUPER SERVER\r\n")
                .append("Connection: Close\r\n")
        return result
    }

    private fun getContentType(format: String): String? = when (format) {
        "txt" -> "text/plain"
        "html" -> "text/html"
        "css" -> "text/css"
        "js" -> "text/javascript"
        "jpg" -> "image/jpeg"
        "jpeg" -> "image/jpeg"
        "gif" -> "image/gif"
        "png" -> "image/png"
        "swf" -> "application/x-shockwave-flash"
        else -> null
    }
}