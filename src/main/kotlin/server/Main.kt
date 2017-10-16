package server

fun main(args: Array<String>) {
    var PORT = 80
    var workers = 1

    for (i in args.indices) {
        if ("-p" == args[i]) {
            PORT = args[i + 1].toInt()
        }
    }
    Server(PORT).start()
}


