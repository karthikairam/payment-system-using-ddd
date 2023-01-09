package com.skiply.system.common.api.jackson.converter.json;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;

public interface ValueObjectJsonConverter<T> {
    JsonDeserializer<T> getJsonDeserializer();
    JsonSerializer<T> getJsonSerializer();
    Class<T> getType();
}
