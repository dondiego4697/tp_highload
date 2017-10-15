package server

import kotlinx.coroutines.experimental.asCoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class Server(private val port: Int) {

    fun start() {
        System.out.println("Server start on $port port")
        val socketServer = ServerSocket(port)
        val config = ConfigParser().parse()
        while (true) {
            analyseRequest(socketServer.accept(), config)
        }
        /*System.out.println("Server start on $port port")
        val socketServer = ServerSocket(port)
        val config = ConfigParser().parse()
        val cpuLimit = Integer.parseInt(config["cpu_limit"])
        val threadPool = ThreadPoolExecutor(cpuLimit, cpuLimit,
                30L, TimeUnit.SECONDS,
                SynchronousQueue())
        while (true) {
            launch(threadPool.asCoroutineDispatcher()) {
                analyseRequest(socketServer.accept(), config)
            }
        }*/
    }

    private /*suspend*/ fun analyseRequest(socket: Socket, config: HashMap<String, String>) {
        val requestLine = BufferedReader(InputStreamReader(socket.getInputStream())).readLine()
        if (requestLine != null) {
            //System.out.println("requestLint = " + requestLine)
            val split = requestLine.split(" ")
            RequestAnalyser(socket, config).analyse(split as ArrayList<String>)
        }
    }
}
