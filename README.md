# Daikon Prometheus

![Daikon](./logo.svg)

Daikon Prometheus is a library that add to Daikon the ability to expose metrics for Prometheus. 

The main goals are:
* Expose useful metrics for Prometheus

## How to add Daikon Prometheus to your project
[![](https://jitpack.io/v/DaikonWeb/daikon-prometheus.svg)](https://jitpack.io/#DaikonWeb/daikon-prometheus)

### Gradle
- Add JitPack in your root build.gradle at the end of repositories:
```
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```
- Add the dependency
```
implementation('com.github.DaikonWeb:daikon-prometheus:1.2.3')
```

### Maven
- Add the JitPack repository to your build file 
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
- Add the dependency
```
<dependency>
    <groupId>com.github.DaikonWeb</groupId>
    <artifactId>daikon-prometheus</artifactId>
    <version>1.2.3</version>
</dependency>
```

## How to use

### To publish metrics:
```
HttpServer()
    .prometheus("/prometheus")
    .start().use {
        assertThat(get("http://localhost:4545/prometheus").text).contains("jvm")
    }
```

### To use a custom metric:
```
HttpServer()
    .prometheus("/prometheus")
    .get("/counter") { _, _, ctx ->
        ctx.meterRegistry().counter("calls").increment()
    }
    .start().use {
        get("http://localhost:4545/counter")
        get("http://localhost:4545/counter")
        assertThat(get("http://localhost:4545/foo").text).contains("calls_total 2.0")
    }
```

## Resources
* Documentation: https://daikonweb.github.io
* Examples: https://github.com/DaikonWeb/daikon-examples
* Micrometer: https://micrometer.io/
* Prometheus: https://github.com/prometheus

## Authors

* **[Daniele Fongo](https://github.com/danielefongo)**

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details
