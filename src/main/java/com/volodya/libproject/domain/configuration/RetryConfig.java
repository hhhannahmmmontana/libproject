package com.volodya.libproject.domain.configuration;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.List;

@Configuration
@EnableRetry
public class RetryConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        return new RetryTemplateBuilder()
            .maxAttempts(3)
            .exponentialBackoff(1000, 2, 7000, true)
            .retryOn(List.of(CannotCreateTransactionException.class))
            .build();
    }

    @Bean
    public RetryOperationsInterceptor retryOperationsInterceptor(RetryTemplate retryTemplate) {
        var interceptor = new RetryOperationsInterceptor();
        interceptor.setRetryOperations(retryTemplate);
        return interceptor;
    }

    @Bean
    public Advisor retryAdvisor(RetryOperationsInterceptor retryOperationsInterceptor) {
        var pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.volodya.libproject.domain.services..*.*(..))");
        return new DefaultPointcutAdvisor(pointcut, retryOperationsInterceptor);
    }
}
