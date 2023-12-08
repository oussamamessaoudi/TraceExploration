package me.oussamamessaoudi;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import me.oussamamessaoudi.loging_config.RA;

@RA
@AllArgsConstructor
public class RA1 {
    private RA3 ra3;
    @SneakyThrows
    public Person exec(Person person) {
        Thread.sleep(2000);
        ra3.exec(person);
        if (person.getName().isBlank()) {
            throw new RuntimeException("Required champ [NAME]");
        }
        return Person.builder()
                .name(person.getName())
                .birthday(person.getBirthday())
                .build();
    }
}
