package shakir.bhav.common

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.wss
import io.ktor.client.request.header
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object FyersOrderSocket {


    var fyersSocket: DefaultClientWebSocketSession? = null
    val httpClient: HttpClient by lazy {
        HttpClient(CIO) {
            install(WebSockets) {
                // Configure WebSockets
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun start() {
        try {
            fyersSocket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        delay(500)
        httpClient.wss(
            method = HttpMethod.Get,
            host = "socket.fyers.in",
            port = 443,
            path = "/trade/v3",
            request = {
                header("authorization", "${settings.FYERS_APP_ID}:${settings.FYERS__access_token}")
            }
        ) {


            fyersSocket = this
            GlobalScope.launch {
                delay(500)
                fyersSocket?.send(
                    """
                {"T": "SUB_ORD", "SLIST": ["orders", "trades", "positions", "edis", "pricealerts", "login"], "SUB_T": 1}
            """.trimIndent()
                )
            }

            GlobalScope.launch {
                while (true) {
                    delay(10000)
                    if (fyersSocket?.isActive == true) {
                        fyersSocket?.send(
                            """PING""".trimIndent()
                        )
                    } else {
                        break
                    }

                }
            }



            while (true) {
                try {
                    if (fyersSocket?.isActive == true) {
                        val text1 = (incoming.receive() as? Frame.Text)?.readText()
                        println("startFyersocket " + text1)
                        val file = getDataFolder("FyersOrderSocket", "Fyers__${milliFileDateTime(System.currentTimeMillis())}")
                        file.writeText(text1 ?: "")
                    } else {
                        break
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }

    fun sennnnd() {}

}
