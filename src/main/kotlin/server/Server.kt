package server

import java.net.ServerSocket

class Server(private val port: Int, private val threadNum: Int, private val root: String) {

    private val buffSize: Int = 10 * 1024 * 1024

    fun start() {
        val serverSocket = ServerSocket(port)
        val threads: List<Thread> = createThreads(serverSocket)
        threads.forEach(Thread::start)
        for (i in 0 until threads.size) {
            threads[i].join()
        }
    }

    private fun createThreads(serverSocket: ServerSocket): List<Thread> {
        val result: ArrayList<Thread> = ArrayList(0)
        var i = 0
        while (i != threadNum) {
            result.add(Thread(Worker(serverSocket, root, buffSize)))
            i++
        }
        return result
    }
}
