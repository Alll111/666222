package com.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 容错解析常见日期字符串，优先兼容标准 yyyy-MM-dd HH:mm:ss。
 */
public class FlexibleDateDeserializer extends JsonDeserializer<Date> {

    private static final List<String> PATTERNS = Arrays.asList(
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd",
            "yyyy-MM-dd EEE HH:mm:ss",
            "yyyy-MM-dd EEEE HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ssX",
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "EEE MMM dd yyyy HH:mm:ss zzz",
            "EEE MMM dd yyyy HH:mm:ss 'GMT'Z"
    );

    @Override
    public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        String text = parser.getText();
        if (text == null) {
            return null;
        }

        String value = text.trim();
        if (value.isEmpty()) {
            return null;
        }

        for (String pattern : PATTERNS) {
            Date parsed = tryParse(value, pattern);
            if (parsed != null) {
                return parsed;
            }
        }

        throw context.weirdStringException(value, Date.class, "expected format \"yyyy-MM-dd HH:mm:ss\"");
    }

    private Date tryParse(String value, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
        formatter.setLenient(false);
        try {
            return formatter.parse(value);
        } catch (ParseException ignored) {
            return null;
        }
    }
}
