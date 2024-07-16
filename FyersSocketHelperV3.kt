package shakir.bhav.common

import com.google.common.primitives.Ints
import com.google.common.primitives.Longs
import com.google.common.primitives.Shorts
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.wss
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readBytes
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import shakir.bhav.common.chartUiCommon.NSECrawler
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


object FyersSocketHelperV3 {


    var source = "PythonSDK-3.0.9"
    var mode = "P"
    private var channelNum: Int = 11


    var process_UI_enabled = false
    var process_UI_function: ((String, Double, Long, Long) -> Unit)? = null


    var process_MCCSH_UI_LOGIC = false
    var process_MCCSH_UI_LOGIC_function: (() -> Unit)? = null

    var klines: ArrayList<FyersSocketKlines> = arrayListOf()


    var fyersSocket: DefaultClientWebSocketSession? = null


    val httpClient: HttpClient by lazy {
        HttpClient(CIO) {
            install(WebSockets) {
                // Configure WebSockets
            }
        }
    }

    var lastHITTime = 0L


    suspend fun subscribe() {
        try {

            if (System.currentTimeMillis() - lastHITTime <= TimeUnit.SECONDS.toMillis(10)) {

                subscribeReal()
            } else {
                startFyersocket()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var listSubscribedList = arrayListOf<String>()
    suspend fun subscribeReal() {
        val symbolToFyersSymbol = FyersMaster.symbolToFyersSymbol()

    }

    suspend fun close() {
        println("FyersSocketHelper : close() called")
        try {
            fyersSocket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun startFyersocket() {
//        if (settings.FYERS__access_token.isNullOrBlank()) {
//            println("startFyersocket Not Logged In")
//            return
//        }

        //println("binary 000")
        val idToSymbol = FyersMaster.fyersIDtoSymbol()
        val symbolToFyersSymbol = FyersMaster.symbolToFyersSymbolV3()

        // println("binary 65656565")
        //wss://socket.fyers.in/hsm/v1-5/prod
        httpClient.wss(
            method = HttpMethod.Get,
            host = "socket.fyers.in",
            port = 443,
            path = "/hsm/v1-5/prod"
        ) {
            try {

                suspend fun sendBinary(binaryData: ByteArray?) {
                    if (binaryData != null) {
                        val milli = System.currentTimeMillis()
                        File(File(getDataFolder("FyersTestV3"), "logs"), milli.div(1000).toString() + "." + milli % 1000 + "send_JAVA.sgdhgs")
                            .writeBytes(binaryData)
                        val frame = Frame.Binary(true, binaryData)
                        send(frame)
                    }
                }

                fyersSocket = this

                GlobalScope.launch {
                    while (true) {
                        sendBinary(ByteArray(1) { 11 })
                         println("send ping")
                        delay(10000)
                    }
                }

                GlobalScope.launch {

                    delay(1000)
                    sendBinary(createAccessTokenMessage())
                    delay(1000)
                    sendBinary(createFullModeMessage())
                    delay(1000)
                    sendBinary( createSubscriptionMessage(klines.map { it.symbol } + Series.getMyListScrips("intra").distinct().filterNotNull().take(200).apply {
                        listSubscribedList.clear()
                        listSubscribedList.addAll(this)
                    }.map {
                        //println(it+" symbolToFyersSymbol "+symbolToFyersSymbol.get(it))
                        "sf|nse_cm|${ symbolToFyersSymbol.get(it)}"
                    }.filterNotNull().take(1)))


                    println("SEND createSubscriptionMessage")
                    println("SEND DONE")
                }
                println("RECIVE REACH")

                val file = getDataFolder("FyersTestV3", "Fyers__${milliFileDateTime(System.currentTimeMillis())}")

                while (true) {
                    val othersMessage = incoming.receive() as? Frame.Binary ?: continue
                    val binary = othersMessage.readBytes()

                    val milli = System.currentTimeMillis()
                    File(File(getDataFolder("FyersTestV3"), "logs"), milli.div(1000).toString() + "." + milli % 1000 + "recv_JAVA.sgdhgs")
                        .writeBytes(binary)


                    println("binary incoming ${binary.size} ${String(binary)}")


                    //val o = ByteBuffer.wrap(byteArray.copyOfRange(index, index + 4)).order(ByteOrder.LITTLE_ENDIAN).getFloat()


                    val responseType = ByteBuffer.wrap(binary.copyOfRange(2, 3)).order(ByteOrder.BIG_ENDIAN).get().toInt()
                    //println("binary  ${(binary.size)} responseType $responseType")
                    if (responseType == (1)) { //auth
                        var offset = 4
                        offset += 1
                        val fieldLength = ByteBuffer.wrap(binary.copyOfRange(5, 7)).order(ByteOrder.BIG_ENDIAN).getShort()
                        offset += 2
                        val stringVal = String(binary.slice(offset until offset + fieldLength).toByteArray(), Charsets.UTF_8)
                        offset += fieldLength
                        //println("binary ${binary.size} responseType $responseType stringVal $stringVal")
                        // println("binary  ${(binary.size)} responseType $responseType fieldLength ${fieldLength} responseType $responseType stringVal $stringVal")
                    } else if (responseType == (4)) { // data feed
                        var offset = 4
                        offset += 1
                        val fieldLength = ByteBuffer.wrap(binary.copyOfRange(5, 7)).order(ByteOrder.BIG_ENDIAN).getShort()
                        offset += 2
                        val stringVal = String(binary.slice(offset until offset + fieldLength).toByteArray(), Charsets.UTF_8)
                        offset += fieldLength
                        //println("binary ${binary.size} responseType $responseType stringVal $stringVal")
                        //println("binary  ${(binary.size)} responseType $responseType fieldLength ${fieldLength} responseType $responseType stringVal $stringVal")


                    } else if (responseType == (6)) { // data feed

                        val scrip_count = ByteBuffer.wrap(binary.copyOfRange(7, 9)).order(ByteOrder.BIG_ENDIAN).getShort()
                        var offset = 9
                        for (j in 0 until scrip_count) {
                            val data_type = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 1)).order(ByteOrder.BIG_ENDIAN).get().toInt()

                            if (data_type == 83) {
                                offset += 1
                                val topic_id = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 2)).order(ByteOrder.BIG_ENDIAN).getShort().toInt()
                                offset += 2
                                val topic_name_len = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 1)).order(ByteOrder.BIG_ENDIAN).get().toInt()
                                offset += 1
                                val topic_name = String(binary.copyOfRange(offset, offset + topic_name_len))
                                offset += topic_name_len
                                if (topic_name.take(2) == "dp") {
                                    //todo depth
                                } else if (topic_name.take(2) == "if") {
                                    //todo index
                                } else if (topic_name.take(2) == "sf") { //stock


                                    val field_count = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 1)).order(ByteOrder.BIG_ENDIAN).get().toInt()
                                    offset += 1
                                    for (i in 0 until field_count) {
                                        val value = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt()
                                        offset += 4
                                        if (value != -2147483648) {
                                            //todo : what is here?
                                        }

                                    }
                                    offset += 2
                                    val multiplier = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 2)).order(ByteOrder.BIG_ENDIAN).getShort().toInt()
                                    offset += 2
                                    val precision = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 1)).order(ByteOrder.BIG_ENDIAN).get().toInt()
                                    offset += 1

                                    var string_len = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 1)).order(ByteOrder.BIG_ENDIAN).get().toInt()
                                    offset += 1
                                    val market = String(binary.copyOfRange(offset, offset + string_len))
                                    /***nse_cm**/
                                    offset += string_len

                                    string_len = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 1)).order(ByteOrder.BIG_ENDIAN).get().toInt()
                                    offset += 1
                                    val stock_id = String(binary.copyOfRange(offset, offset + string_len))
                                    /***17963**/
                                    offset += string_len

                                    string_len = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 1)).order(ByteOrder.BIG_ENDIAN).get().toInt()
                                    offset += 1
                                    val stock_name = String(binary.copyOfRange(offset, offset + string_len))
                                    /***NESTLEIND-EQ**/
                                    offset += string_len

                                    println("topic_id $topic_id topic_name $topic_name stock_name $stock_name")
                                    scrip_topics[topic_id] = stock_name.split("-")[0]

                                }


                            } else if (data_type == 85) {
                                println("data_type $data_type 85")
                                offset += 1
                                val topic_id = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 2)).order(ByteOrder.BIG_ENDIAN).getShort().toInt()
                                offset += 2
                                val field_count = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 1)).order(ByteOrder.BIG_ENDIAN).get().toInt()
                                offset += 1
                                var sf_flag = false;
                                var idx_flag = false;
                                var dp_flag = false;
                                var UpdateTick = false

                                println("data_type $data_type 85 field_count $field_count")

                                for (i in 0 until field_count){
                                    val inttt = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt().times(1000)
                                    val ffff = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getFloat()
                                    offset += 4
                                    println("$i int $inttt ffff $ffff")
                                }

//                                    val ftm0 = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt().times(1000)
//                                    offset += 4
//                                    val dtm1 = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt().times(1000)
//                                    offset += 4
//                                    val fdtm = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt().times(1000)
//                                    offset += 4
//                                    val ltt = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt().times(1000)
//                                    offset += 4
//                                    val v = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt()
//                                    offset += 4
//                                    val ltp = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getFloat()
//                                    offset += 4
//
//                                    val ltq = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt()
//                                    offset += 4
//                                    val tbq = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt()
//                                    offset += 4
//
//                                    val tsq = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt()
//                                    offset += 4
//                                    val bp = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getFloat()
//                                    offset += 4
//                                    val sp = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getFloat()
//                                    offset += 4
//                                    val bq = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt()
//                                    offset += 4
//                                    val bs = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt()
//                                    offset += 4
//                                    val ap = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getFloat()
//                                    offset += 4
//
//                                    val lo = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getFloat()
//                                    offset += 4
//                                    val h = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getFloat()
//                                    offset += 4
//
//                                    val lcl = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getFloat()
//                                    offset += 4
//                                    val ucl = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getFloat()
//                                    offset += 4
//                                    val yh = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getFloat()
//                                    offset += 4
//
//                                    val yl = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getFloat()
//                                    offset += 4
//                                    val op = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getFloat()
//                                    offset += 4
//                                    val c = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getFloat()
//                                    offset += 4
//
//                                    val oi = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt()
//                                    offset += 4



                            } else if (data_type == 76) {
                                //todo lite mode
                            }
                        }
                    }


                    file.appendBytes(Ints.toByteArray(binary.size))
                    file.appendBytes(binary)
                    // decode(binary, idToSymbol)
                    lastHITTime = System.currentTimeMillis()

                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


    val type_L1_7208 = 7208


    var stopTest = false
    var pauseTest = false




    val list = HashMap<String, Int>()
    val scrip_topics = HashMap<Int, String>()





    fun createSubscriptionMessage(symbols: List<String>): ByteArray {
        //  return  File("/Users/shakir/BhavAppData/_NSE_/FyersTestV3/logs/1721105619.4615848sub.sgdhgs").readBytes()

        // var symbols= arrayOf("sf|nse_cm|25","sf|nse_cm|3045")
        try {
            val stream2 = ByteArrayOutputStream()
            stream2.writeByte((symbols.size shr 8 and 0xFF).toByte())
            stream2.writeByte((symbols.size and 0xFF).toByte())

            println("scrip scrip scrip ${symbols.joinToString(",,,")}")
            symbols.forEach { scrip ->
                val scripBytes = scrip.toByteArray(Charsets.US_ASCII)
                stream2.writeByte(scripBytes.size.toByte())
                stream2.write(scripBytes)
            }

            val dataLen = 18 + stream2.size() + settings.FYERS__access_token.length + source.length

            val stream = ByteArrayOutputStream()
            stream.writeShort(dataLen.toShort())
            stream.writeByte(4)
            stream.writeByte(2)
            stream.writeByte(1)
            stream.writeShort(stream2.size().toShort())
            stream.write(stream2.toByteArray())
            stream.writeByte(2)
            stream.writeShort(1)
            stream.writeByte(channelNum.toByte())

            //File("/Users/shakir/BhavAppData/_NSE_/FyersTestV3/logs/1721105619.4615848sub_JJJJJJJJ.sgdhgs").writeBytes(stream.toByteArray())

            //println(File("/Users/shakir/BhavAppData/_NSE_/FyersTestV3/logs/1721105619.4615848sub.sgdhgs").readBytes().toHexString())
            println(stream.toByteArray().toHexString())
            return stream.toByteArray()


        } catch (e: Exception) {
            throw e // Re-throw the exception for further handling
        }
    }

    fun ByteArray.toHexString(): String {
        return joinToString(" ") { "%02x".format(it.toInt()) }
    }


    @OptIn(ExperimentalEncodingApi::class)
    fun convertAccessTokenToHsmToken(): String? {
        try {
            val tokens = settings.FYERS__access_token.split(".")
            println("tokens√√")
            println(tokens.joinToString("tokens: \n==="))
//            if (tokens.size != 3) {
//                FyersAPIHelper.logout()
//                return null
//            }
            //println("decodedPayload ${tokens[1].length}")

            try {
                println(
                    Base64.UrlSafe.decode(tokens[1] + "").decodeToString()
                )

                println(
                    Base64.UrlSafe.decode(tokens[0] + "").decodeToString()
                )

                println(
                    Base64.UrlSafe.decode(tokens[2] + "").decodeToString()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }


            //val decodedHeader = Base64.UrlSafe.decode(tokens[0] + "===").decodeToString()
            val decodedPayload = Base64.UrlSafe.decode(tokens[1] + "").decodeToString()

            // val decodedToken = Json.decodeFromString<Map<String, Any>>(decodedPayload)
            val decodedToken = JSONObject(decodedPayload)

            val expiration = decodedToken.getLong("exp")
            println("expiration ${milliFileDateTime(expiration.times(1000))}")

            if (expiration - System.currentTimeMillis() / 1000 < 0) {
                FyersAPIHelper.logout()
                return null
            }
            val hsm_key = decodedToken.getString("hsm_key")
            println("hsm_key ${decodedToken} ${hsm_key}")
            return hsm_key
        } catch (e: Exception) {
            e.printStackTrace()
            //onError(buildError(defines.AUTH_TYPE, defines.INVALID_CODE, defines.INVALID_TOKEN))
            return null
        }
    }

    fun ByteArray.writeShort(offset: Int, value: Short) {
        //todo shr was called on short
        this[offset] = (value.toInt() shr 8).toByte()
        this[offset + 1] = value.toByte()
    }


    private fun createAccessTokenMessage(): ByteArray {
        println("ByteOrder.nativeOrder() ${ByteOrder.nativeOrder()} ByteOrder.BIG_ENDIAN ${ByteOrder.BIG_ENDIAN} ByteOrder.LITTLE_ENDIAN ${ByteOrder.LITTLE_ENDIAN}")

        val stream = ByteArrayOutputStream()
        val hsmToken = convertAccessTokenToHsmToken()!!
        println("hsmToken hsmToken ${hsmToken}")
        val bufferSize = 18 + hsmToken.length + source.length
        stream.write(ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort((bufferSize - 2).toShort()).array())

        stream.write(ByteBuffer.allocate(1).order(ByteOrder.BIG_ENDIAN).put((1).toByte()).array())
        stream.write(ByteBuffer.allocate(1).order(ByteOrder.BIG_ENDIAN).put((4).toByte()).array())
        stream.write(ByteBuffer.allocate(1).order(ByteOrder.BIG_ENDIAN).put((1).toByte()).array())
        stream.write(ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(hsmToken.length.toShort()).array())
        stream.write(hsmToken.toByteArray())

        stream.write(ByteBuffer.allocate(1).order(ByteOrder.BIG_ENDIAN).put((2).toByte()).array())
        stream.write(ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(mode.length.toShort()).array())
        stream.write(mode.toByteArray(Charsets.UTF_8))

        stream.write(ByteBuffer.allocate(1).order(ByteOrder.BIG_ENDIAN).put((3).toByte()).array())
        stream.write(ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(1).array())
        stream.write(ByteBuffer.allocate(1).order(ByteOrder.BIG_ENDIAN).put((1).toByte()).array())

        stream.write(ByteBuffer.allocate(1).order(ByteOrder.BIG_ENDIAN).put((4).toByte()).array())
        stream.write(ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(source.length.toShort()).array())
        stream.write(source.toByteArray())
        return stream.toByteArray()
    }



    fun ByteArrayOutputStream.writeShort(Short: Short) {
        write(ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(Short).array())
    }

    fun ByteArrayOutputStream.writeByte(bite: Byte) {
        write(ByteBuffer.allocate(1).order(ByteOrder.BIG_ENDIAN).put(bite).array())
    }

    fun ByteArrayOutputStream.writeLong(long: Long) {
        write(ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putLong(long).array())
    }


    private fun createFullModeMessage(): ByteArray {


        val channels = listOf(channelNum)
        val stream = ByteArrayOutputStream()
        stream.writeShort((0).toShort())
        stream.writeByte((12).toByte())
        stream.writeByte((2).toByte())
        var channelBits = 0L
        for (channelNum in channels) {
            if (channelNum in 1..63) {
                channelBits = channelBits or (1L shl channelNum)
            }
        }

        stream.writeByte((1).toByte())
        stream.writeShort((8).toShort())
        stream.writeByte((channelBits).toByte())
        stream.writeByte((2).toByte())
        stream.writeShort((1).toShort())
        stream.writeByte((70).toByte())
        return stream.toByteArray()
    }


    fun channelResume(): ByteArray {
        val channels = listOf(channelNum)
        val stream = ByteArrayOutputStream()
        stream.writeShort((0).toShort())
        stream.writeByte(8)
        stream.writeByte(1)
        var channelBits = 0L
        for (channelNum in channels) {
            if (channelNum in 1..63) {
                channelBits = channelBits or (1L shl channelNum)
            }
        }
        stream.writeByte(1)
        stream.writeShort((8).toShort())
        stream.writeLong((channelBits).toLong())
        return stream.toByteArray()
    }

    fun channelPause(): ByteArray {
        val channels = listOf(channelNum)
        val stream = ByteArrayOutputStream()
        stream.writeShort((0).toShort())
        stream.writeByte(7)
        stream.writeByte(1)
        var channelBits = 0L
        for (channelNum in channels) {
            if (channelNum in 1..63) {
                channelBits = channelBits or (1L shl channelNum)
            }
        }
        stream.writeByte(1)
        stream.writeShort((8).toShort())
        stream.writeLong((channelBits).toLong())
        return stream.toByteArray()
    }


}
