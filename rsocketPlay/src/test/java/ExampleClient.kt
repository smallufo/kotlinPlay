/**
 * Created by kevin.huang on 2019-10-07.
 */

import io.rsocket.Payload
import io.rsocket.RSocket
import io.rsocket.core.RSocketConnector
import io.rsocket.transport.netty.client.WebsocketClientTransport
import io.rsocket.util.DefaultPayload
import reactor.core.publisher.Flux
import java.net.URI

object ExampleClient {
  @JvmStatic
  fun main(args: Array<String>) {
    val ws = WebsocketClientTransport.create(URI.create("ws://rsocket-demo.herokuapp.com/ws"))
    val client: RSocket = RSocketConnector.connectWith(ws).block()!!
//    val client = RSocketFactory.connect()
//      .keepAlive(Duration.ofSeconds(20), Duration.ofSeconds(30), 3)
//      .transport(ws).start().block()!!

    try {
      val s: Flux<Payload> = client.requestStream(DefaultPayload.create("peace"))

      s.take(20).doOnNext { p -> println(p.dataUtf8) }.blockLast()
    } finally {
      client.dispose()
    }
  }
}