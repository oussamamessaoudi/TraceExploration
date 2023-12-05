package me.oussamamessaoudi;

import io.micrometer.tracing.Tracer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
@RestController
@AllArgsConstructor
public class Controller {
    RA1 ra1;
    RA2 ra2;
    RA4 ra4;
    Tracer tracer;
    TaskExecutor taskExecutor;


    @GetMapping
    public String get(@RequestParam("name") String name, @RequestParam("birthday") String birthday) {
        final Function<String, Person> builder = (s) -> Person.builder().name(name + " " + s).birthday(birthday).build();
        try {
            Stream.<Runnable>of(
                            () -> {
                                ra1.exec(builder.apply("RA1.1"));
                            }, () -> {
                                ra2.exec(builder.apply("RA2.1"), "Hello");
                            }, () -> {
                                ra1.exec(builder.apply("RA1.2"));
                            }, () -> {
                                ra4.execute("RA4", "RA4", "hello");
                            })
                    //.parallel()
                    .forEach(runnable -> taskExecutor.execute(runnable));
        } catch (Exception e) {

        }
        return tracer.currentSpan().context().traceId();
    }

}

