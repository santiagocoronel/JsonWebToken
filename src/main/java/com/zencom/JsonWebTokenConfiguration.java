package com.zencom;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;

import javax.validation.constraints.*;
import java.io.UnsupportedEncodingException;

public class JsonWebTokenConfiguration extends Configuration {

    @NotEmpty
    private String jwtTokenSecret = "dfwzsdzwh823zebdwdz772632gdsbddfwzsdzwh823zebdwdz772632gdsbd";

    public byte[] getJwtTokenSecret() throws UnsupportedEncodingException {
        return jwtTokenSecret.getBytes("UTF-8");
    }

}
