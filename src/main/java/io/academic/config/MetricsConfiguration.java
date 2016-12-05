package io.academic.config;


import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableMetrics(proxyTargetClass = true)
public class MetricsConfiguration extends MetricsConfigurerAdapter {

    private MetricRegistry metricRegistry = new MetricRegistry();

    @Override
    @Bean
    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }


}