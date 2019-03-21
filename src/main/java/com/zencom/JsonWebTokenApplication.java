package com.zencom;


import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import com.zencom.auth.Account;
import com.zencom.auth.AccountAuthenticator;
import com.zencom.resources.AccountResources;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

public class JsonWebTokenApplication extends Application<JsonWebTokenConfiguration> {

    public static void main(final String[] args) throws Exception {
        new JsonWebTokenApplication().run(args);
    }

    @Override
    public String getName() {
        return "JsonWebToken";
    }

    @Override
    public void initialize(final Bootstrap<JsonWebTokenConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final JsonWebTokenConfiguration configuration,
                    final Environment environment) {

        try {
            initializeJwtAuthenticator(configuration,environment);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void initializeJwtAuthenticator(final JsonWebTokenConfiguration configuration, final Environment environment) throws UnsupportedEncodingException {
        final byte[] key = configuration.getJwtTokenSecret();

        final JwtConsumer consumer = new JwtConsumerBuilder()
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setRequireSubject() // the JWT must have a subject claim
                .setVerificationKey(new HmacKey(key)) // verify the signature with the public key
                .setRelaxVerificationKeyValidation() // relaxes key length requirement
                .build(); // create the JwtConsumer instance

        environment.jersey().register(new AuthDynamicFeature(
                new JwtAuthFilter.Builder<Account>()
                        .setJwtConsumer(consumer)
                        .setRealm("realm")
                        .setPrefix("Bearer")
                        .setAuthenticator(new AccountAuthenticator())
                        .buildAuthFilter()));

        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(Principal.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AccountResources(configuration.getJwtTokenSecret()));
    }

}
