package shakir.bhav.common

import com.google.common.primitives.Ints
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.wss
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readBytes
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.TimeUnit
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.pow


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

        val symbolToFyersSymbol = FyersMaster.symbolToFyersSymbolV3()
        /*****  CAPITAL MARKET SYMBOLS MASTER CSV FILE : https://public.fyers.in/sym_details/NSE_CM.csv */
        /*****  here the hashmap contain  13th column as key and 12th column as value*/
        /*****  ``` EXAMPLE : symbolToFyersSymbol[NESTLEIND]=17963 */

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
                    sendBinary(createSubscriptionMessage(klines.map { it.symbol } + Series.getMyListScrips("intra").distinct().filterNotNull().take(200).apply {
                        listSubscribedList.clear()
                        listSubscribedList.addAll(this)
                    }.map {
                        //println(it+" symbolToFyersSymbol "+symbolToFyersSymbol.get(it))
                        "sf|nse_cm|${symbolToFyersSymbol.get(it)}"
                    }.filterNotNull().take(1)))


                    println("SEND createSubscriptionMessage")
                    println("SEND DONE")
                }
                println("RECIVE REACH")

                val file = getDataFolder("FyersTestV3", "Fyers__${milliFileDateTime(System.currentTimeMillis())}")

                while (true) {
                    val othersMessage = incoming.receive() as? Frame.Binary ?: continue
                    val binary = othersMessage.readBytes()
                    decode(binary)
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


    fun decode(binary: ByteArray) {


        val responseType = ByteBuffer.wrap(binary.copyOfRange(2, 3)).order(ByteOrder.BIG_ENDIAN).get().toInt()
        //println("binary ${String(binary)}  ${(binary.size)} responseType $responseType")
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

                        //println("topic_id $topic_id topic_name $topic_name stock_name $stock_name multiplier $multiplier precision $precision market $market stock_id $stock_id")
                        symbols_of_topics[topic_id] = stock_name.split("-")[0]
                        multiplier_topics[topic_id] = multiplier
                        precision_topics[topic_id] = precision

                    }


                } else if (data_type == 85) {

                    offset += 1
                    val topic_id = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 2)).order(ByteOrder.BIG_ENDIAN).getShort().toInt()
                    offset += 2
                    val field_count = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 1)).order(ByteOrder.BIG_ENDIAN).get().toInt()
                    offset += 1
                    var sf_flag = false;
                    var idx_flag = false;
                    var dp_flag = false;
                    var UpdateTick = false


                    var last_traded_price = parsePrice(binary, offset, topic_id)
                    offset += 4
                    var trade_volume = parseQty(binary, offset, topic_id)
                    offset += 4
                    var last_traded_time = parseTimeMilliseconds(binary, offset, topic_id)
                    offset += 4
                    var exchange_feed_time = parseTimeMilliseconds(binary, offset, topic_id)
                    offset += 4
                    var best_bid_size = parseQty(binary, offset, topic_id)
                    offset += 4
                    var best_offer_size = parseQty(binary, offset, topic_id)
                    offset += 4
                    var best_bid_price = parsePrice(binary, offset, topic_id)
                    offset += 4
                    var best_offer_price = parsePrice(binary, offset, topic_id)
                    offset += 4
                    var last_traded_quantity = parseQty(binary, offset, topic_id)
                    offset += 4
                    var total_buy_quantity = parseQty(binary, offset, topic_id)
                    offset += 4
                    var total_sell_quantity =parseQty(binary, offset, topic_id)
                    offset += 4
                    var vwap_varage_price = parsePrice(binary, offset, topic_id)
                    offset += 4
                    var open_interest = parseQty(binary, offset, topic_id) //what is this? is this applicable in stocks?
                    offset += 4
                    var low_price = parsePrice(binary, offset, topic_id)
                    offset += 4
                    var high_price = parsePrice(binary, offset, topic_id)
                    offset += 4
                    var week52_high = parsePrice(binary, offset, topic_id)
                    offset += 4
                    var week52_low = parsePrice(binary, offset, topic_id)
                    offset += 4
                    var lower_circuite = parsePrice(binary, offset, topic_id)
                    offset += 4
                    var upper_circuite = parsePrice(binary, offset, topic_id)
                    offset += 4
                    var open_price = parsePrice(binary, offset, topic_id)
                    offset += 4
                    var previous_close_price = parsePrice(binary, offset, topic_id)
                    offset += 4

                    println("last_traded_price ${last_traded_price.printFloat()} ${open_price.printFloat()} ${high_price.printFloat()} ${low_price.printFloat()} ${previous_close_price.printFloat()}  $trade_volume ${milliDisplay(last_traded_time)}")
                    //todo create OHLCV class
                    //why they don't provide OHLC of a smaller time frame, ??????

                    //todo : parse properly using loop; above one may fail if field_count!=21;
//                    for (i in 0 until field_count){
//                        val inttt = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt()
//                        val intttUUUU = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt().toUInt()
//                        val ffff = ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getFloat()
//                        offset += 4
//                        println("${symbols_of_topics.get(topic_id)} $i int $inttt $intttUUUU ffff $ffff")
//                        if (i==3){
//                            ltt=inttt.times(1000L)
//                        } else if (i==4){
//                            v=inttt.toLong()
//                        }
//                    }


                } else if (data_type == 76) {
                    //todo lite mode
                }
            }
        }

    }


    val type_L1_7208 = 7208


    var stopTest = false
    var pauseTest = false


    val list = HashMap<String, Int>()
    val symbols_of_topics = HashMap<Int, String>()
    val multiplier_topics = HashMap<Int, Int>()
    val precision_topics = HashMap<Int, Int>()


    fun createSubscriptionMessage(symbols: List<String>): ByteArray {
        // var symbols= arrayOf("sf|nse_cm|25","sf|nse_cm|3045")
        try {
            val stream2 = ByteArrayOutputStream()
            stream2.writeByte((symbols.size shr 8 and 0xFF).toByte())
            stream2.writeByte((symbols.size and 0xFF).toByte())
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
            return stream.toByteArray()


        } catch (e: Exception) {
            e.printStackTrace()
            throw e // Re-throw the exception for further handling
        }
    }




    @OptIn(ExperimentalEncodingApi::class)
    fun convertAccessTokenToHsmToken(): String? {
        try {
            val tokens = settings.FYERS__access_token.split(".")
            val decodedPayload = Base64.UrlSafe.decode(tokens[1] + "").decodeToString()
            val decodedToken = JSONObject(decodedPayload)
            val expiration = decodedToken.getLong("exp")
            if (expiration - System.currentTimeMillis() / 1000 < 0) {
                FyersAPIHelper.logout()
                return null
            }
            val hsm_key = decodedToken.getString("hsm_key")
            return hsm_key
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }




    private fun createAccessTokenMessage(): ByteArray {
       val stream = ByteArrayOutputStream()
        val hsmToken = convertAccessTokenToHsmToken()!!
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


    //todo call
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

    //todo call
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


    fun parsePrice(binary: ByteArray, offset: Int, topic_id: Int): Double {
        return ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt().toUInt().toLong()
            .toDouble()
            .times(multiplier_topics[topic_id]!!.toDouble())
            .div(10.0.pow(precision_topics[topic_id]!!))
    }

    fun parseQty(binary: ByteArray, offset: Int, topic_id: Int): Long {
        return ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt().toUInt().toLong()
    }

    fun parseTimeMilliseconds(binary: ByteArray, offset: Int, topic_id: Int): Long {
        return ByteBuffer.wrap(binary.copyOfRange(offset, offset + 4)).order(ByteOrder.BIG_ENDIAN).getInt().toUInt().toLong().times(1000)
    }


    fun test() {
        val ba = File("/Users/shakir/Downloads/Fyers__2024_07_18__13_19_20").readBytes()
        var ib = 0

        while (true) {
            try {
                val length = Ints.fromByteArray(ba.sliceArray(ib..ib + 3))
                ib += 4
                val ca = ba.sliceArray(ib until ib + length)
                decode(ca)
                ib += length


            } catch (e: Exception) {
                e.printStackTrace()
                break
            }
        }
    }



}


