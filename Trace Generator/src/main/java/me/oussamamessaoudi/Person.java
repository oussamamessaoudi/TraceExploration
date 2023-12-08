package me.oussamamessaoudi;

import lombok.Builder;
import lombok.Data;
import me.oussamamessaoudi.LogingRaConfig.ConfidentialField;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class Person {
    private String name;
    @ConfidentialField
    private String birthday;
    @Builder.Default
    private int[] arrayInt = new int[]{1, 2};
    @Builder.Default
    private Integer[] arrayInteger = new Integer[]{3, 3};
    @Builder.Default
    private List<Integer> listInteger = List.of(3, 3);
    @Builder.Default
    private Map<Integer, String> mapInteger = Map.of(3, "3", 4, "4");
    @ConfidentialField("£££££")
    @Builder.Default
    private double confidentialWithValue;
}
