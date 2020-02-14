package daikon.prometheus

import daikon.HttpServer
import khttp.get
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PrometheusMetricsTest {

    @Test
    fun `can publish meters`() {
        HttpServer(5555)
            .prometheus("/foo")
            .start().use {
                assertThat(get("http://localhost:5555/foo").text).contains("jvm")
            }
    }
}