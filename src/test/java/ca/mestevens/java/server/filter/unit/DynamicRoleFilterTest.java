package ca.mestevens.java.server.filter.unit;

import ca.mestevens.java.UnitTest;
import ca.mestevens.java.server.filter.DynamicRoleFilter;
import lombok.SneakyThrows;
import lombok.Value;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.security.Principal;
import java.util.*;

@Category(UnitTest.class)
public class DynamicRoleFilterTest {

    @Test
    @SneakyThrows
    public void replaceOneProperty() {

        final String propertyValue = UUID.randomUUID().toString();
        final String replacementString = "user:%s:read";
        final TestContainer testContainer = new TestContainer("user", propertyValue, String.format(replacementString, propertyValue));

        final DynamicRoleFilter dynamicRoleFilter = new DynamicRoleFilter(String.format(replacementString, "{user}"));
        dynamicRoleFilter.filter(testContainer.getContainerRequestContext());

    }

    @Test
    @SneakyThrows
    public void replaceMultipleProperties() {

        final String replacementString = "user:%s:%s:read";
        final Map<String, String> properties = new HashMap<>();
        final String userReplacement = "23";
        final String nameReplacement = "userName";
        properties.put("user", userReplacement);
        properties.put("name", nameReplacement);
        final TestContainer testContainer = new TestContainer(properties, String.format(replacementString, userReplacement, nameReplacement));

        final DynamicRoleFilter dynamicRoleFilter = new DynamicRoleFilter(String.format(replacementString, "{user}", "{name}"));
        dynamicRoleFilter.filter(testContainer.getContainerRequestContext());

    }

    @Test(expected = ForbiddenException.class)
    @SneakyThrows
    public void replaceUnknownProperty() {

        final String propertyValue = UUID.randomUUID().toString();
        final String replacementString = "user:%s:read";
        final TestContainer testContainer = new TestContainer("user", propertyValue, String.format(replacementString, propertyValue));

        final DynamicRoleFilter dynamicRoleFilter = new DynamicRoleFilter(String.format(replacementString, "{usear}"));
        dynamicRoleFilter.filter(testContainer.getContainerRequestContext());

    }

    @Test
    @SneakyThrows
    public void noReplacements() {

        final TestContainer testContainer = new TestContainer("users:read");

        final DynamicRoleFilter dynamicRoleFilter = new DynamicRoleFilter("users:read");
        dynamicRoleFilter.filter(testContainer.getContainerRequestContext());

    }

    @Test(expected = ForbiddenException.class)
    @SneakyThrows
    public void userHasNoPermissionsNoReplacements() {

        final TestContainer testContainer = new TestContainer();

        final DynamicRoleFilter dynamicRoleFilter = new DynamicRoleFilter("users:read");
        dynamicRoleFilter.filter(testContainer.getContainerRequestContext());

    }

    @Test(expected = ForbiddenException.class)
    @SneakyThrows
    public void userHasNoPermissionsWithReplacements() {

        final String userId = UUID.randomUUID().toString();
        final String replacementString = "users:%s:read";
        final TestContainer testContainer = new TestContainer("user", userId);

        final DynamicRoleFilter dynamicRoleFilter = new DynamicRoleFilter(String.format(replacementString, "{user}"));
        dynamicRoleFilter.filter(testContainer.getContainerRequestContext());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void efficientSecurityCheck() {

        final List roleList = Mockito.mock(List.class);
        final DynamicRoleFilter dynamicRoleFilter = new DynamicRoleFilter(roleList);
        final ContainerRequestContext containerRequestContext = Mockito.mock(ContainerRequestContext.class);
        final UriInfo uriInfo = Mockito.mock(UriInfo.class);
        final SecurityContext securityContext = new TestSecurityContext(null);

        Mockito.when(containerRequestContext.getUriInfo())
                .thenReturn(uriInfo);
        Mockito.when(uriInfo.getPathParameters())
                .thenReturn(new MultivaluedHashMap<>());
        Mockito.when(containerRequestContext.getSecurityContext())
                .thenReturn(securityContext);

        boolean exceptionThrown = false;
        try {
            dynamicRoleFilter.filter(containerRequestContext);
        } catch (final Exception ex) {
            Assert.assertTrue(ex instanceof ForbiddenException);
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);

        Mockito.verifyZeroInteractions(roleList);

    }

    @Value
    private static class TestContainer {

        private final ContainerRequestContext containerRequestContext;

        private final UriInfo uriInfo;

        private final MultivaluedMap<String, String> properties;

        private final List<String> rolesSubstitution;

        private final Principal testPrincipal;

        private final SecurityContext securityContext;

        private TestContainer() {

            this.containerRequestContext = Mockito.mock(ContainerRequestContext.class);
            this.uriInfo = Mockito.mock(UriInfo.class);
            this.properties = new MultivaluedHashMap<>();
            this.rolesSubstitution = new ArrayList<>();
            this.testPrincipal = new TestPrincipal(UUID.randomUUID().toString());
            this.securityContext = new TestSecurityContext(testPrincipal);

            setupMocks();

        }

        private TestContainer(final String roleSubstitution) {

            this.containerRequestContext = Mockito.mock(ContainerRequestContext.class);
            this.uriInfo = Mockito.mock(UriInfo.class);
            this.properties = new MultivaluedHashMap<>();
            this.rolesSubstitution = Collections.singletonList(roleSubstitution);
            this.testPrincipal = new TestPrincipal(UUID.randomUUID().toString());
            this.securityContext = new TestSecurityContext(testPrincipal, roleSubstitution);

            setupMocks();

        }

        private TestContainer(final String propertyName,
                             final String propertyValue) {

            this.containerRequestContext = Mockito.mock(ContainerRequestContext.class);
            this.uriInfo = Mockito.mock(UriInfo.class);
            this.properties = new MultivaluedHashMap<>();
            this.properties.putSingle(propertyName, propertyValue);
            this.rolesSubstitution = new ArrayList<>();
            this.testPrincipal = new TestPrincipal(UUID.randomUUID().toString());
            this.securityContext = new TestSecurityContext(testPrincipal);

            setupMocks();

        }

        private TestContainer(final String propertyName,
                             final String propertyValue,
                             final String roleSubstitution) {

            this.containerRequestContext = Mockito.mock(ContainerRequestContext.class);
            this.uriInfo = Mockito.mock(UriInfo.class);
            this.properties = new MultivaluedHashMap<>();
            this.properties.putSingle(propertyName, propertyValue);
            this.rolesSubstitution = Collections.singletonList(roleSubstitution);
            this.testPrincipal = new TestPrincipal(UUID.randomUUID().toString());
            this.securityContext = new TestSecurityContext(testPrincipal, roleSubstitution);

            setupMocks();

        }

        private TestContainer(final Map<String, String> properties,
                             final String... roleSubstitutions) {
            this(properties, Arrays.asList(roleSubstitutions));
        }

        private TestContainer(final Map<String, String> properties,
                             final List<String> rolesSubstitution) {

            this.containerRequestContext = Mockito.mock(ContainerRequestContext.class);
            this.uriInfo = Mockito.mock(UriInfo.class);
            this.properties = new MultivaluedHashMap<>(properties);
            this.rolesSubstitution = rolesSubstitution;
            this.testPrincipal = new TestPrincipal(UUID.randomUUID().toString());
            this.securityContext = new TestSecurityContext(testPrincipal, rolesSubstitution);

            setupMocks();

        }

        private void setupMocks() {
            Mockito.when(containerRequestContext.getUriInfo())
                    .thenReturn(uriInfo);
            Mockito.when(uriInfo.getPathParameters())
                    .thenReturn(properties);
            Mockito.when(containerRequestContext.getSecurityContext())
                    .thenReturn(securityContext);
        }

    }

    @Value
    private static class TestPrincipal implements Principal {

        private final String name;

        private TestPrincipal(final String name) {
            this.name = name;
        }

    }

    @Value
    private static class TestSecurityContext implements SecurityContext {

        private final Principal userPrincipal;

        private final List<String> userRoles;

        private final String authenticationScheme;

        private TestSecurityContext(final Principal userPrincipal,
                                   final String... userRoles) {
            this.userPrincipal = userPrincipal;
            this.userRoles = Arrays.asList(userRoles);
            this.authenticationScheme = "";
        }

        private TestSecurityContext(final Principal userPrincipal,
                                   final List<String> userRoles) {
            this.userPrincipal = userPrincipal;
            this.userRoles = userRoles;
            this.authenticationScheme = "";
        }

        @Override
        public boolean isUserInRole(final String s) {
            return userRoles.contains(s);
        }

        @Override
        public boolean isSecure() {
            return false;
        }

    }

}
