package server

fun main(args: Array<String>) {
    val config = ConfigParser().parse()
    val port = Integer.parseInt(config["listen"])
    val root = config["document_root"]
    val threadNum = Integer.parseInt(config["cpu_limit"])
    val cpusAvailable = Runtime.getRuntime().availableProcessors()

    System.out.println("Server start on $port port")
    System.out.println("available = $cpusAvailable, got = $threadNum")
    Server(port, threadNum, root!!).start()
}


