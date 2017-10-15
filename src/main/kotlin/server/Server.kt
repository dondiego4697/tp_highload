package server

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket

class Server(private val port: Int) {

    fun start() {
        System.out.println("Server start on $port port")
        val socketServer = ServerSocket(port)
        val config = ConfigParser().parse()
        while (true) {
            launch(CommonPool) {
                analyseRequest(socketServer.accept(), config)
            }
        }
    }

    private suspend fun analyseRequest(socket: Socket, config: HashMap<String, String>) {
        val requestLine = BufferedReader(InputStreamReader(socket.getInputStream())).readLine()
        if (requestLine != null) {
            //System.out.println("requestLint = " + requestLine)
            val split = requestLine.split(" ")
            RequestAnalyser(socket, config).analyse(split as ArrayList<String>)
        }
    }
}
