package ca.mestevens.java.dropwizard.auth;

import lombok.Value;

import java.security.Principal;
import java.util.List;

@Value
public class TestUser implements Principal {

    private final String name;

    private final List<String> roles;

}
