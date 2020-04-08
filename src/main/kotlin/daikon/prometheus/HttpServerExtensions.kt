package daikon.prometheus

import daikon.HttpServer
import daikon.core.Context
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.eclipse.jetty.http.MimeTypes.Type.TEXT_PLAIN_UTF_8

fun HttpServer.prometheus(path: String, config: PrometheusConfig = PrometheusConfig.DEFAULT): HttpServer {
    afterStart { ctx ->
        val registry = PrometheusMeterRegistry(config)

        JvmThreadMetrics().bindTo(registry)
        JvmMemoryMetrics().bindTo(registry)
        JvmGcMetrics().bindTo(registry)

        ProcessorMetrics().bindTo(registry)
        UptimeMetrics().bindTo(registry)

        ctx.addAttribute("prometheusRegistry", registry)
    }
    get(path) {
        _, res, ctx ->
        res.type(TEXT_PLAIN_UTF_8.asString())
        res.write(ctx.meterRegistry().scrape())
    }
    return this
}

fun Context.meterRegistry(): PrometheusMeterRegistry {
    return getAttribute("prometheusRegistry")
}