package me.oussamamessaoudi;

import lombok.SneakyThrows;

@RA
public class RA2 {
    @SneakyThrows
    public Person exec(Person person, String value) {
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
