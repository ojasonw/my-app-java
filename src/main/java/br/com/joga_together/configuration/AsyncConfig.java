package br.com.joga_together.configuration;

import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

public class AsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);    // threads sempre vivas esperando trabalho
        executor.setMaxPoolSize(20);    // máximo de threads simultâneas
        executor.setQueueCapacity(100); // fila se todas as 20 estiverem ocupadas
        executor.setThreadNamePrefix("email-async-");
        executor.initialize();
        return executor;
    }
}
