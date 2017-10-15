package server

fun main(args: Array<String>) {
    var PORT = 8080

    args.indices
            .filter { "-p" == args[it] }
            .forEach { PORT = args[it + 1].toInt() }
    Server(PORT).start()
}


