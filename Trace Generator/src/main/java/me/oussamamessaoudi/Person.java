package me.oussamamessaoudi;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {
    private String name;
    private String birthday;
}
