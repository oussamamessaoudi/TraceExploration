package me.oussamamessaoudi;

import lombok.SneakyThrows;
import me.oussamamessaoudi.loging_config.RA;

@RA
public class RA3 {
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
