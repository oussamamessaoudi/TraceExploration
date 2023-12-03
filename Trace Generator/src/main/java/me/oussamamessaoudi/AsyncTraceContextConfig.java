package me.oussamamessaoudi;

import io.micrometer.context.ContextSnapshot;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@AllArgsConstructor
public class AsyncTraceContextConfig {

    @Bean
    public TaskDecorator taskDecorator() {
        return (runnable) -> ContextSnapshot.captureAll(new Object[0]).wrap(runnable);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setTaskDecorator(taskDecorator());
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }
}
