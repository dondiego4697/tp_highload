package sample


fun main(args: Array<String>) {
    var PORT = 80

    for (i in args.indices) {
        if ("-p" == args[i]) {
            PORT = args[i + 1].toInt()
        }
    }
    Server(PORT).start()
}


