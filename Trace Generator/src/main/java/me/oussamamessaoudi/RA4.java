package me.oussamamessaoudi;


import lombok.SneakyThrows;
import me.oussamamessaoudi.loging_config.ConfidentialField;
import me.oussamamessaoudi.loging_config.RA;

@RA
public class RA4 {

    @SneakyThrows
    public void execute(@ConfidentialField String fieldConfidential,
                        String field,
                        @ConfidentialField("$$$$$") String fieldConfidentialPersonalise) {
        Thread.sleep(2000);
    }
}
