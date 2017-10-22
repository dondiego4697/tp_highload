package server

import kotlinx.coroutines.experimental.*
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
        val cpus = Runtime.getRuntime().availableProcessors()
        val cpuT = if (cpus < Integer.parseInt(config["cpu_limit"])) cpus else Integer.parseInt(config["cpu_limit"])
        System.out.println("available = $cpus, got = $cpuT")
        val threadPool = ThreadPoolExecutor(cpuT, cpuT,
                60L, TimeUnit.SECONDS,
                SynchronousQueue())
        while (true) {
            analyseRequest(socketServer.accept(), config, threadPool)
        }
    }

    private fun analyseRequest(socket: Socket, config: HashMap<String, String>, threadPool: ThreadPoolExecutor) {
        val requestLine = BufferedReader(InputStreamReader(socket.getInputStream())).readLine()
        if (requestLine != null) {
            //System.out.println("requestLint = " + requestLine)
            val split = requestLine.split(" ")
            launch(threadPool.asCoroutineDispatcher()) {
                RequestAnalyser(socket, config).analyse(split as ArrayList<String>)
            }
        }
    }
}
