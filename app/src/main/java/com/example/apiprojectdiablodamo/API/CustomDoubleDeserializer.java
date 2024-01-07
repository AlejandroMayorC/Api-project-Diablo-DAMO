package com.example.apiprojectdiablodamo.API;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class CustomDoubleDeserializer implements JsonDeserializer<Double> {
    @Override
    public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String numberString = json.getAsString();
        try {
            return Double.parseDouble(numberString.replace(",", ""));
        } catch (NumberFormatException e) {
            throw new JsonParseException("Could not parse double: " + numberString, e);
        }
    }
}


