package server

import java.io.File
import java.net.Socket
import java.net.URLDecoder

class RequestAnalyser(private val socket: Socket, private val CONFIG: HashMap<String, String>) {

    private val availableMethods = arrayListOf("GET", "HEAD")
    private val rootPath = CONFIG["document_root"]

    suspend fun analyse(split: ArrayList<String>) {
        try {
            val method: String = split[0]
            if (availableMethods.indexOf(method) == -1 || split.size < 3) {
                Response(socket.getOutputStream(), Status.METHOD_NOT_ALLOWED).send()
                return
            }
            val path: String = split[1]
            var url = URLDecoder.decode(path, "UTF-8")
            url = rootPath + url
            url = url.substringBefore('?')

            val isIndex = url.endsWith('/')
            if (isIndex) {
                url += "index.html"
            }
            val file = File(url)
            //System.out.println("AbsolutePath = " + file.absolutePath)
            //System.out.println("CanonicalPath = " + file.canonicalPath)
            if (!file.canonicalPath.contains(rootPath.toString())) {
                Response(socket.getOutputStream(), Status.FORBIDDEN).send()
                return
            }
            if (file.isFile) {
                Response(socket.getOutputStream(), Status.OK).send(file, "GET" == method)
                return
            }
            if (isIndex) {
                Response(socket.getOutputStream(), Status.FORBIDDEN).send()
                return
            }
            Response(socket.getOutputStream(), Status.NOT_FOUND).send()
        } finally {
            socket.close()
        }
    }
}