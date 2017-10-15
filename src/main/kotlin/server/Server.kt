package server

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
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
            analyseRequest(socketServer.accept(), config)
        }
    }

    private fun analyseRequest(socket: Socket, config: HashMap<String, String>) = runBlocking(CommonPool) {
        val requestLine = BufferedReader(InputStreamReader(socket.getInputStream())).readLine()
        if (requestLine != null) {
            //System.out.println("requestLint = " + requestLine)
            val split = requestLine.split(" ")
            val job = launch(CommonPool) {
                RequestAnalyser(socket, config).analyse(split as ArrayList<String>)
            }
            job.join()
        }
    }
}
