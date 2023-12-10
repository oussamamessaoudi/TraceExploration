package me.oussamamessaoudi;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.regex.Pattern;

public class JsonMessageConverter extends ClassicConverter {

    private final ObjectMapper objectMapper;
    private final ObjectWriter objectWriter;
    private final Pattern pattern = Pattern.compile("\"(\\w+)\" :");

    public JsonMessageConverter() {
        this.objectMapper = new ObjectMapper();
        this.objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

    }

    @Override
    public String convert(ILoggingEvent event) {
        String formattedMessage = event.getFormattedMessage();
        try {
            JsonNode jsonNode = objectMapper.readTree(formattedMessage);
            String prettyJson = objectWriter.writeValueAsString(jsonNode);
            return System.lineSeparator() + colorizeJson(prettyJson);
        } catch (IOException e) {
            return formattedMessage;
        }
    }

    private String colorizeJson(String jsonString) {
        String resetColor = "\u001B[0m";
        String color = "\u001B[31m";
        String format = "%s\"$1\"%s :";
        return pattern
                .matcher(jsonString)
                .replaceAll(String.format(format, color, resetColor));
    }
}
