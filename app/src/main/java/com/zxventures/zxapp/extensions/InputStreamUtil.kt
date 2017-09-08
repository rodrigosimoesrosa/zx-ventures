package br.com.mirabilis.util.io

import org.json.JSONObject
import org.w3c.dom.Document
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import javax.xml.parsers.DocumentBuilderFactory

object InputStreamUtil {

    fun UTF8 () = "UTF-8"

    fun toBytes(input: InputStream): ByteArray {
        val bos = ByteArrayOutputStream()
        val buf = ByteArray(1024)
        try {
            while (true){
                val length = input.read(buf)
                if (length <=0) break
                bos.write(buf, 0, length)
            }
            val bytes = bos.toByteArray()
            return bytes
        } finally {
            bos.flush()
            bos.close()
        }
    }

    fun toJSON(input: InputStream): JSONObject {
        val reader = BufferedReader(InputStreamReader(input, UTF8()))
        val stringBuilder = StringBuilder()
        while (true){
            val temp:String? = reader.readLine()
            if(temp == null) break
            stringBuilder.append(temp)
        }
        return JSONObject(stringBuilder.toString())
    }

    fun toString(input: InputStream): String {
        return String(toBytes(input))
    }

    fun toXML(input: InputStream?): String {
        return toDocumentXML(input).toString()
    }

    fun toDocumentXML(input: InputStream?): Document {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input)
    }
}