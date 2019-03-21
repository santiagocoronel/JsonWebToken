package com.zencom.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.zencom.auth.Account;
import io.dropwizard.auth.Auth;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

@Path("/v1/accounts")
public class AccountResources {

    private final byte[] tokenSecret;

    public AccountResources(byte[] tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    @GET
    @Path("/generate-expired-token")
    public Map<String, String> generateExpiredToken() {
        final JwtClaims claims = new JwtClaims();
        claims.setExpirationTimeMinutesInTheFuture(-20);
        claims.setSubject("good-guy");

        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(tokenSecret));

        try {
            return singletonMap("token", jws.getCompactSerialization());
        } catch (JoseException e) {
            throw Throwables.propagate(e);
        }
    }

    @GET
    @Path("/generate-valid-token")
    public Map<String, String> generateValidToken() {
        final JwtClaims claims = new JwtClaims();
        claims.setSubject("good-guy");
        claims.setExpirationTimeMinutesInTheFuture(30);

        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(tokenSecret));

        try {
            return singletonMap("token", jws.getCompactSerialization());
        } catch (JoseException e) {
            throw Throwables.propagate(e);
        }
    }

    @GET
    @Path("/check-token")
    public Map<String, Object> get(@Auth Principal user) {
        return ImmutableMap.<String, Object>of("username", user.getName(), "id", ((Account) user).getId());
    }

    @Timed
    @POST
    @Path("/signIn")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(@HeaderParam("basicAuth") String basicAuth,
                           @HeaderParam("email") String email,
                           @HeaderParam("password") String password) {

        if (basicAuth == null || email == null || password == null)
            throw new WebApplicationException(Response.status(403).build());

        if (!basicAuth.equals("36956501"))
            throw new WebApplicationException(Response.status(403).build());

        if (email.equals("santiagocoronel@hotmail.es") && password.equals("Qwerty123@")) {


            final JwtClaims claims = new JwtClaims();
            claims.setSubject("santiagocoronel@hotmail.es");
            claims.setExpirationTimeMinutesInTheFuture(30);

            final JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(claims.toJson());
            jws.setAlgorithmHeaderValue(HMAC_SHA256);
            jws.setKey(new HmacKey(tokenSecret));

            try {
                return Response.ok().entity(jws.getCompactSerialization()).build();
            } catch (JoseException e) {
                throw Throwables.propagate(e);
            }
        }

        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataUser(@Auth Principal user) {
        System.out.println(user.toString());
        return Response.ok().entity(user).build();
    }

}
