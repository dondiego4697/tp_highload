package server

import kotlinx.coroutines.experimental.launch
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

class Worker(private val serverSocket: ServerSocket, private val root: String, private val buffSize: Int) : Runnable {
    override fun run() {
        try {
            while (true) {
                val socket: Socket = getSocket()
                analyseRequest(socket)
            }
        } catch (e: Exception) {
            System.out.println(e)
        }
    }

    private fun getSocket(): Socket {
        return serverSocket.accept()
    }

    private fun analyseRequest(socket: Socket) {
        launch {
            RequestAnalyser(socket, root, buffSize).analyse()
        }
    }
}