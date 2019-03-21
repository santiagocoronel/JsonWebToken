package com.zencom.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtContext;

import java.util.Optional;

public class AccountAuthenticator implements Authenticator<JwtContext, Account> {

    @Override
    public Optional<Account> authenticate(JwtContext context) throws AuthenticationException {
        try {

            final String subject = context.getJwtClaims().getSubject();

            if ("santiagocoronel@hotmail.es".equals(subject)) {
                return Optional.of(new Account(new Long(1), "Santiago", "Coronel", "santiagocoronel@hotmail.es"));
            }

            return Optional.empty();
        } catch (MalformedClaimException e) {
            return Optional.empty();
        }
    }
}
