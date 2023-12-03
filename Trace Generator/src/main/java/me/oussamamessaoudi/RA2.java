package me.oussamamessaoudi;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class RA2 {
    @SneakyThrows
    public Person exec(Person person) {
        Thread.sleep(3000);
        if (person.getBirthday() == null || person.getBirthday().isBlank()) {
            throw new RuntimeException("Required champ [BIRTHDAY]");
        }
        return Person.builder()
                .name(person.getName())
                .birthday(person.getBirthday())
                .build();
    }
}
