package server

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class ConfigParser {

    fun parse(): HashMap<String, String> {
        val result = HashMap<String, String>()
        val path = "./httpd.conf"
        val lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8)
        lines
                .map { it.split(" ") }
                .forEach { result.put(it[0], it[1]) }
        return result
    }
}