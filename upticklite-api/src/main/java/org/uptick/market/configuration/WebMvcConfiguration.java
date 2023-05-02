package org.uptick.market.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    public WebMvcConfiguration() {
        super();
    }

    @Autowired
    private LoggerInterceptor loggerInterceptor;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/static/**", "/lib/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/")
                .addResourceLocations("classpath:/lib/")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/public");
        super.addResourceHandlers(registry);
    }


    /**
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggerInterceptor).addPathPatterns("/api/**");

    }


    /**
     *
     */
    @Bean
    public FilterRegistrationBean parameterFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new RequestParameterFilter());
        registration.addUrlPatterns("/api/1.0/user/save.do");
        registration.addUrlPatterns("/api/1.0/nft/mint.do");
        registration.addUrlPatterns("/api/1.0/nft/deploy.do");


//        registration.setName("parameterFilter");
        return registration;

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();


        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(BigDecimal.class, new BigDecimalSerialize());
        simpleModule.addSerializer(Double.class, new BigDecimalSerialize());
        simpleModule.addSerializer(Double.TYPE, new BigDecimalSerialize());

        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
    }


    /**
     * money serializer.
     */
    public class BigDecimalSerialize extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            BigDecimal bigDecimal = BigDecimal.ZERO;
            if (value != null) {
                if (value instanceof BigDecimal) {
                    bigDecimal = (BigDecimal) value;
                }
                if (value instanceof Double) {
                    bigDecimal = new BigDecimal(value.toString());
                }

            }
            gen.writeString(bigDecimal.stripTrailingZeros().toPlainString());
        }
    }

    /**
     * money serializer.
     */
    public class MoneySerialize extends JsonSerializer<Object> {
        final BigDecimal TEMP = BigDecimal.valueOf(1000000L);

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value != null) {
                BigDecimal bigDecimal = new BigDecimal(value.toString());
                gen.writeNumber(bigDecimal.divide(TEMP, 4, RoundingMode.DOWN));
            }
        }
    }

    /**
     * double serializer.
     */
    public class DoubleSerialize extends JsonSerializer<Double> {

        private DecimalFormat df = new DecimalFormat("##.00");

        @Override
        public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException, JsonProcessingException {
            if (value != null) {
                gen.writeString(df.format(value));
            }
        }
    }
}
