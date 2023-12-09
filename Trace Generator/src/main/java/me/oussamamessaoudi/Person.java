package me.oussamamessaoudi;

import lombok.Builder;
import lombok.Data;
import me.oussamamessaoudi.loging_config.ConfidentialField;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class Person {
    private String name;
    @ConfidentialField(size = 6)
    private String birthday;
    @Builder.Default
    private int[] arrayInt = new int[]{1, 2};
    @Builder.Default
    private Integer[] arrayInteger = new Integer[]{3, 3};
    @Builder.Default
    private List<Integer> listInteger = List.of(3, 3);
    @Builder.Default
    private Map<Integer, String> mapInteger = Map.of(3, "3", 4, "4");
    @ConfidentialField(value = '£')
    @Builder.Default
    private double confidentialWithValue;
}
