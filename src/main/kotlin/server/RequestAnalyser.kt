package server

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.Socket
import java.net.URLDecoder


class RequestAnalyser(private val socket: Socket, private val root: String) {

    private val availableMethods = arrayListOf("GET", "HEAD")

    fun analyse() {
        //System.out.println(Thread.currentThread().name)
        val inputStream = socket.getInputStream()
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val requestLine = bufferedReader.readLine()
        val outputStream = socket.getOutputStream()

        try {
            if (requestLine == null) return
            val split = requestLine.split(" ") as ArrayList<String>

            val method: String = split[0]
            if (!checkMethod(method, split)) return

            val path: String = split[1]
            var url = getUrl(path)
            val isIndex = isIndex(url)
            if (isIndex) {
                url += "index.html"
            }
            val file = File(url)
            val status: Status = checkFile(file, isIndex)
            if (status == Status.OK) {
                Response(outputStream, status).send(file, "GET" == method)
            } else {
                Response(outputStream, status).send()
            }
        } catch (e: Exception) {
            System.out.println(e)
        } finally {
            bufferedReader.close()
            inputStream.close()
            outputStream.close()
            socket.close()
        }
    }

    private fun checkFile(file: File, isIndex: Boolean): Status {
        if (!file.canonicalPath.contains(root)) {
            return Status.FORBIDDEN
        }

        if (file.isFile) {
            return Status.OK
        }

        if (isIndex) {
            return Status.FORBIDDEN
        }
        return Status.NOT_FOUND
    }

    private fun checkMethod(method: String, split: ArrayList<String>): Boolean {
        if (availableMethods.indexOf(method) == -1 || split.size < 3) {
            Response(socket.getOutputStream(), Status.METHOD_NOT_ALLOWED).send()
            return false
        }
        return true
    }

    private fun isIndex(url: String): Boolean {
        return url.endsWith('/')
    }

    private fun getUrl(path: String): String {
        var url = URLDecoder.decode(path, "UTF-8")
        url = root + url
        return url.substringBefore('?')
    }
}
