package me.oussamamessaoudi;


import lombok.SneakyThrows;

@RA
public class RA4 {

    @SneakyThrows
    public void execute(@ConfidentialField String fieldConfidential, String field) {
        Thread.sleep(2000);
    }
}
