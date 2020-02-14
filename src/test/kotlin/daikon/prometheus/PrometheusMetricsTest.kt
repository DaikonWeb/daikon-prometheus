package daikon.prometheus

import daikon.HttpServer
import khttp.get
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PrometheusMetricsTest {

    @Test
    fun `can publish metrics`() {
        HttpServer(5555)
            .prometheus("/foo")
            .start().use {
                assertThat(get("http://localhost:5555/foo").text).contains("jvm")
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
                get("http://localhost:5555/bar")
                get("http://localhost:5555/bar")
                assertThat(get("http://localhost:5555/foo").text).contains("calls_total 2.0")
            }
    }
}