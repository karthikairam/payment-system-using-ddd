package com.skiply.system.common.api.jackson.converter.json;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import org.springframework.format.Formatter;

public interface ValueObjectJsonConverter<T> {
    JsonDeserializer<T> getJsonDeserializer();
    JsonSerializer<T> getJsonSerializer();
    Formatter<T> getTypedFieldFormatter();
    Class<T> getType();
}
