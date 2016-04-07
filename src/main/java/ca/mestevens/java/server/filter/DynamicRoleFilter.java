package ca.mestevens.java.server.filter;

import org.glassfish.jersey.server.internal.LocalizationMessages;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DynamicRoleFilter implements ContainerRequestFilter {

    private final List<String> rolesAllowed;

    public DynamicRoleFilter(final String... rolesAllowed) {
        this(Arrays.asList(rolesAllowed));
    }

    public DynamicRoleFilter(final List<String> rolesAllowed) {
        this.rolesAllowed = rolesAllowed;
    }

    public void filter(final ContainerRequestContext containerRequestContext) throws IOException {

        final MultivaluedMap<String, String> propertyNames = containerRequestContext.getUriInfo().getPathParameters();

        final SecurityContext securityContext = containerRequestContext.getSecurityContext();

        final boolean authorized = securityContext.getUserPrincipal() != null &&
                rolesAllowed.stream()
                        .map(role -> propertyNames.keySet().stream()
                                .filter(role::contains)
                                .reduce(role, (r1, r2) -> r1.replace(String.format("{%s}", r2), propertyNames.getFirst(r2))))
                        .anyMatch(securityContext::isUserInRole);

        if (!authorized) {
            throw new ForbiddenException(LocalizationMessages.USER_NOT_AUTHORIZED());
        }

    }

}
