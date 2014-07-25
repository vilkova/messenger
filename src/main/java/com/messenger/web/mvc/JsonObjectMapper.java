package com.messenger.web.mvc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created with IntelliJ IDEA.
 * User: Nastya Vilkova
 */
public class JsonObjectMapper extends ObjectMapper {
    public JsonObjectMapper()
    {
        super();
        setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
    }
}
