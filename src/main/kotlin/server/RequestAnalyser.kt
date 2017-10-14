package server

import java.io.File
import java.net.Socket
import java.net.URLDecoder

class RequestAnalyser(private val socket: Socket) {

    private val availableMethods = arrayListOf("GET", "HEAD")

    private var CONFIG = ConfigParser().parse()
    private val rootPath = CONFIG["document_root"]

    fun analyse(method: String, path: String) {
        try {
            if (availableMethods.indexOf(method) == -1) {
                Response(socket.getOutputStream(), Status.METHOD_NOT_ALLOWED).send()
                return
            }
            var url = URLDecoder.decode(path, "UTF-8")
            System.out.println(rootPath)
            url = rootPath + url
            url = url.substringBefore('?')

            val isIndex = url.endsWith('/')
            if (isIndex) {
                url += "index.html"
            }
            val file = File(url)
            System.out.println(file.absolutePath)
            if (file.isFile) {
                Response(socket.getOutputStream(), Status.OK).send(file, "GET" == method)
                return
            }
            if (isIndex) {
                Response(socket.getOutputStream(), Status.FORBIDDEN).send()
                return
            }
            Response(socket.getOutputStream(), Status.NOT_FOUND).send()
        } catch (e: Exception) {
            System.out.println(e)
        } finally {
            socket.close()
        }
    }
}