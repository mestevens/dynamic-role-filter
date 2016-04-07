package ca.mestevens.java.dropwizard.auth;

import io.dropwizard.auth.Authorizer;

public class TestAuthorizer implements Authorizer<TestUser> {

    @Override
    public boolean authorize(TestUser user, String role) {
        return user.getRoles().contains(role);
    }

}
