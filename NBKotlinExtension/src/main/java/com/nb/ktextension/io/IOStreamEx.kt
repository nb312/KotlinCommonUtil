package com.busd.lightwallet.extension

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.min

/**
 * Created by NieBin on 18-9-28
 * Github: https://github.com/nb312
 * Email: niebin312@gmail.com
 */
/**byte 数组的长度*/
private const val BYTE_SIZE = 1024

/**写入字符串*/
fun OutputStream.write2String(content: String) {
    write2Bytes(content.toByteArray())
}

/**通过缓冲流写入byte数组,未关闭流*/
fun OutputStream.write2Bytes(bytes: ByteArray) {
    var bufStream = BufferedOutputStream(this)
    val size = BYTE_SIZE //缓存大小
    var loc = 0 //当前位置
    var len = bytes.size // 数组的长度
    while (len > 0) {
        var off = loc * size
        var minNum = min(size, len - off)
        var copy = bytes.copyOfRange(loc * size, loc * size + minNum)
        bufStream.write(copy)
        len -= minNum
        loc++
    }
    bufStream.flush()
}

/**将留转换为String*/
val InputStream.content2String: String
    get() = content2ByteArray.toString(Charsets.UTF_8)

/**inputStream 转换为 byteArray*/
val InputStream.content2ByteArray: ByteArray
    get() {
        var bufferStream = BufferedInputStream(this) //处理流，缓冲流
        var byteList = mutableListOf<Byte>()
        var bufBytes = ByteArray(BYTE_SIZE)
        var doneIndex = bufferStream.read(bufBytes, 0, BYTE_SIZE)
        if (doneIndex != -1) {
            byteList.addAll(bufBytes.toMutableList().subList(0, doneIndex))
        }
        while (doneIndex != -1 && doneIndex == BYTE_SIZE) {
            doneIndex = bufferStream.read(bufBytes)
            byteList.addAll(bufBytes.toMutableList().subList(0, doneIndex))
        }
        return byteList.toByteArray()
    }