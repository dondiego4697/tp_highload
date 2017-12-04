package server

import kotlinx.coroutines.experimental.*
import java.net.ServerSocket
import java.net.Socket

class Server(private val port: Int, threadNum: Int, private val root: String) {
    private val pool = newFixedThreadPoolContext(threadNum, "pool")
    //private val pool = newSingleThreadContext("MyEventThread")

    /*fun start() {
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
            result.add(Thread(Worker(serverSocket, root)))
            i++
        }
        return result
    }*/

    fun start() {
        /*while(true) {
            launch(pool) {
                aasd()
            }
        }*/
        val serverSocket = ServerSocket(port)
        while (true) {
            val socket: Socket = serverSocket.accept()
            analyseRequest(socket)
        }
    }

    private fun analyseRequest(socket: Socket) = launch(pool) {
        RequestAnalyser(socket, root).analyse()
    }

    /*private fun aasd() {
        var a = "qwe"
        for (i in 1..10000) {
            a = (a + i).hashCode().hashCode().hashCode().toString()
        }
    }*/

    /*private fun createThreads(): List<CoroutineContext> {
        val result: ArrayList<CoroutineContext> = ArrayList(0)
        var i = 0
        while (i != threadNum) {
            result.add(newSingleThreadContext("thread" + i))
            i++
        }
        return result
    }*/
}
