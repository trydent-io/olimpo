package io.trydent.mirandola

import io.vertx.core.AbstractVerticle
import io.vertx.core.Verticle
import io.vertx.core.Vertx.vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router.router
import org.slf4j.LoggerFactory.getLogger
import java.lang.Integer.getInteger

interface Json {
  val asString: String
  val asBuffer: Buffer
}

data class Message(private val message: String) : Json {
  override val asString get() = JsonObject().put("message", message).toString()

  override val asBuffer: Buffer get() = JsonObject().put("message", message).toBuffer()
}

class HttpServerVerticle : AbstractVerticle(), Verticle {
  private val log = getLogger(javaClass)

  override fun start() {
    val httpServer = vertx.createHttpServer()
    val router = router(vertx)

    router.get("/")
      .produces("application/json")
      .handler {
        log.info("Received request on root.")
        it.response()
          .putHeader("content-type", "application/json")
          .end(Message("Hello world").asBuffer)
      }

    httpServer.requestHandler(router).listen(getInteger(System.getenv("PORT")), "0.0.0.0")
  }
}

private val log = getLogger("MAIN")

fun main() {
  val vertx = vertx()
  vertx.deployVerticle(HttpServerVerticle()) {
    it
      .takeIf { result -> result.succeeded() }
      .also { log.info("HttpServer started.") }
      ?: log.error("HttpServer didn't start: ${it.cause().message}")
  }
}