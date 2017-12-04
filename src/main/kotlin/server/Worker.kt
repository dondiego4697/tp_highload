package server

import kotlinx.coroutines.experimental.launch
import java.net.ServerSocket
import java.net.Socket

class Worker(private val serverSocket: ServerSocket, private val root: String) : Runnable {
    override fun run() {
        while (true) {
            val socket: Socket = getSocket()
            analyseRequest(socket)
        }
    }

    private fun getSocket(): Socket {
        return serverSocket.accept()
    }

    private fun analyseRequest(socket: Socket) = launch {
        RequestAnalyser(socket, root).analyse()
    }
}