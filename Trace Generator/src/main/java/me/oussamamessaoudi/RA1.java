package me.oussamamessaoudi;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class RA1 {
    @SneakyThrows
    public Person exec(Person person) {
        Thread.sleep(2000);
        if (person.getName().isBlank()) {
            throw new RuntimeException("Required champ [NAME]");
        }
        return Person.builder()
                .name(person.getName())
                .birthday(person.getBirthday())
                .build();
    }
}
