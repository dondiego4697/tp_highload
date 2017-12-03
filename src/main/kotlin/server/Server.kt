package server

import kotlinx.coroutines.experimental.asCoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newCoroutineContext
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class Server(private val port: Int, private val threadNum: Int, private val root: String) {

    private val buffSize: Int = 10 * 1024 * 1024

    fun start() {
        val serverSocket = ServerSocket(port)

        val threadPool = ThreadPoolExecutor(threadNum, threadNum,
                60L, TimeUnit.SECONDS,
                SynchronousQueue())
        while (true) {
            val socket: Socket = getSocket(serverSocket)
            analyseRequest(socket, threadPool)
        }
    }

    private fun analyseRequest(socket: Socket, threadPool: ThreadPoolExecutor) {
        launch(threadPool.asCoroutineDispatcher()) {
            RequestAnalyser(socket, root, buffSize).analyse()
        }
    }

    private fun getSocket(serverSocket: ServerSocket): Socket {
        return serverSocket.accept()
    }
}
