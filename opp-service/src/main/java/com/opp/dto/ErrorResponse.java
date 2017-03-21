package com.opp.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

import static com.opp.util.DomainUtil.mapEnums;

/**
 * Created by ctobe on 6/13/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    public enum Type {
        GENERAL("general");

        private static final Map<String, Type> JSON_VALUE_MAP = mapEnums(Type.class, Type::getJsonValue);

        @JsonCreator
        public static Type fromJsonValue(String jsonValue) {
            return JSON_VALUE_MAP.get(jsonValue);
        }

        private final String jsonValue;

        Type(String jsonValue) {
            this.jsonValue = jsonValue;
        }

        @JsonValue
        public String getJsonValue() {
            return jsonValue;
        }
    }

    private final int status;
    private final Type type;
    private final String message;

    public ErrorResponse(int status, String message) {
        this(status, Type.GENERAL, message);
    }

    public ErrorResponse(int status, Type type, String message) {
        this.status = status;
        this.type = type;
        this.message = message;
    }

    @ApiModelProperty(value = "The HTTP status code")
    public int getStatus() {
        return status;
    }

    @ApiModelProperty(value = "The error message")
    public String getMessage() {
        return message;
    }

    @ApiModelProperty(value = "The error type")
    public Type getType() {
        return type;
    }
}

