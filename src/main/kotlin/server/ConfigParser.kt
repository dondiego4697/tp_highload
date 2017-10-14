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
            return result
        }
        val lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8)
        for (line in lines) {
            val split = line.split(" ")
            if (split[0] == "document_root") {
                result.put(split[0], split[1] + "/httptest")
            } else {
                result.put(split[0], split[1])
            }
        }
        return result
    }
}