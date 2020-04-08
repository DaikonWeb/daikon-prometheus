package daikon.prometheus

import daikon.HttpServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import topinambur.http

class PrometheusMetricsTest {

    @Test
    fun `can publish metrics`() {
        HttpServer(5555)
            .prometheus("/foo")
            .start().use {
                assertThat("http://localhost:5555/foo".http.get().body).contains("jvm")
            }
    }

    @Test
    fun `custom metrics`() {
        HttpServer(5555)
            .prometheus("/foo")
            .get("/bar") { _, _, ctx ->
                ctx.meterRegistry().counter("calls").increment()
            }
            .start().use {
                "http://localhost:5555/bar".http.get()
                "http://localhost:5555/bar".http.get()
                assertThat("http://localhost:5555/foo".http.get().body).contains("calls_total 2.0")
            }
    }
}