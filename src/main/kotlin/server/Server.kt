package server

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket

class Server(private val port: Int) {

    fun start() {
        System.out.println("Server start on $port port")
        val socketServer = ServerSocket(port)
        while (true) {
            analyseRequest(socketServer.accept())
        }
    }

    private fun analyseRequest(socket: Socket) {
        val requestLine = BufferedReader(InputStreamReader(socket.getInputStream())).readLine()
        if (requestLine != null) {
            System.out.println("requestLint = " + requestLine)
            val split = requestLine.split(" ")
            RequestAnalyser(socket).analyse(split[0], split[1])
        }
    }
}
