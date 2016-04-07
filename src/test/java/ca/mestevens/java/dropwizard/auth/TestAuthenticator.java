package ca.mestevens.java.dropwizard.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Collections;
import java.util.Optional;

public class TestAuthenticator implements Authenticator<BasicCredentials, TestUser> {

    @Override
    public Optional<TestUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
        return Optional.of(new TestUser(credentials.getUsername(), Collections.singletonList(String.format("user:%s:write", credentials.getUsername()))));
    }

}
