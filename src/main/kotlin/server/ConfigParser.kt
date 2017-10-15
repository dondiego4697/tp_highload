package server

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class ConfigParser {

    fun parse(): HashMap<String, String> {
        val result = HashMap<String, String>()
        val path = "/etc/httpd.conf"
        val confFile = File(path)
        if (!confFile.exists()) {
            result.put("document_root", "httptest")
            result.put("cpu_limit", "4")
            return result
        }
        val lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8)
        for (line in lines) {
            val split = line.split(" ")
            result.put(split[0], split[1])
        }
        return result
    }
}