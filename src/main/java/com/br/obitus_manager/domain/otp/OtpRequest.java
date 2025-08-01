package com.br.obitus_manager.domain.otp;

import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OtpCreateRequest.class, name = "CREATE"),
        @JsonSubTypes.Type(value = OtpValidateRequest.class, name = "VALIDATE")
})
public interface OtpRequest {
    OtpRequestType getType();
}
