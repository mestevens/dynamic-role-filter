package ca.mestevens.java.server.filter.integration;

import ca.mestevens.java.IntegrationTest;
import ca.mestevens.java.dropwizard.TestApplication;
import ca.mestevens.java.dropwizard.TestConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.internal.util.Base64;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Category(IntegrationTest.class)
public class DynamicRolesIntegrationTest {

    private static Client client;

    @ClassRule
    public static final DropwizardAppRule<TestConfiguration> dropwizardAppRule =
            new DropwizardAppRule<>(TestApplication.class);

    @BeforeClass
    public static void setUpClass() {
        client = new JerseyClientBuilder(dropwizardAppRule.getEnvironment()).build("TestClient");
    }

    @Test
    public void rolesAllowedReplacement() {

        final String userId = UUID.randomUUID().toString();
        final String password = UUID.randomUUID().toString();

        final Response response = client.target(
                String.format("http://localhost:%d/endpoint/%s", dropwizardAppRule.getLocalPort(), userId))
                .request()
                .header("Authorization", String.format("Basic %s", Base64.encodeAsString(String.format("%s:%s", userId, password))))
                .get();

        Assert.assertEquals(200, response.getStatus());

    }

    @Test
    public void rolesAllowedReplacementInvalidUserId() {

        final String userId = UUID.randomUUID().toString();
        final String password = UUID.randomUUID().toString();

        final Response response = client.target(
                String.format("http://localhost:%d/endpoint/%s", dropwizardAppRule.getLocalPort(), UUID.randomUUID().toString()))
                .request()
                .header("Authorization", String.format("Basic %s", Base64.encodeAsString(String.format("%s:%s", userId, password))))
                .get();

        Assert.assertEquals(403, response.getStatus());

    }

    @Test
    public void rolesAllowedNoReplacementUserId() {

        final String userId = UUID.randomUUID().toString();
        final String password = UUID.randomUUID().toString();

        final Response response = client.target(
                String.format("http://localhost:%d/endpoint2/%s", dropwizardAppRule.getLocalPort(), userId))
                .request()
                .header("Authorization", String.format("Basic %s", Base64.encodeAsString(String.format("%s:%s", userId, password))))
                .get();

        Assert.assertEquals(403, response.getStatus());

    }

    @Test
    public void rolesAllowedNoReplacementInvalidUserId() {

        final String userId = UUID.randomUUID().toString();
        final String password = UUID.randomUUID().toString();

        final Response response = client.target(
                String.format("http://localhost:%d/endpoint2/%s", dropwizardAppRule.getLocalPort(), UUID.randomUUID().toString()))
                .request()
                .header("Authorization", String.format("Basic %s", Base64.encodeAsString(String.format("%s:%s", userId, password))))
                .get();

        Assert.assertEquals(403, response.getStatus());

    }

    @Test
    public void rolesAllowedNoPathParamForReplacementUserId() {

        final String userId = UUID.randomUUID().toString();
        final String password = UUID.randomUUID().toString();

        final Response response = client.target(
                String.format("http://localhost:%d/endpoint3/%s", dropwizardAppRule.getLocalPort(), userId))
                .request()
                .header("Authorization", String.format("Basic %s", Base64.encodeAsString(String.format("%s:%s", userId, password))))
                .get();

        Assert.assertEquals(403, response.getStatus());

    }

    @Test
    public void rolesAllowedNoPathParamForReplacementInvalidUserId() {

        final String userId = UUID.randomUUID().toString();
        final String password = UUID.randomUUID().toString();

        final Response response = client.target(
                String.format("http://localhost:%d/endpoint3/%s", dropwizardAppRule.getLocalPort(), UUID.randomUUID().toString()))
                .request()
                .header("Authorization", String.format("Basic %s", Base64.encodeAsString(String.format("%s:%s", userId, password))))
                .get();

        Assert.assertEquals(403, response.getStatus());

    }

    @Test
    public void permitAllUserId() {

        final String userId = UUID.randomUUID().toString();
        final String password = UUID.randomUUID().toString();

        final Response response = client.target(
                String.format("http://localhost:%d/endpoint4/%s", dropwizardAppRule.getLocalPort(), userId))
                .request()
                .header("Authorization", String.format("Basic %s", Base64.encodeAsString(String.format("%s:%s", userId, password))))
                .get();

        Assert.assertEquals(200, response.getStatus());

    }

    @Test
    public void permitAllInvalidUserId() {

        final String userId = UUID.randomUUID().toString();
        final String password = UUID.randomUUID().toString();

        final Response response = client.target(
                String.format("http://localhost:%d/endpoint4/%s", dropwizardAppRule.getLocalPort(), UUID.randomUUID().toString()))
                .request()
                .header("Authorization", String.format("Basic %s", Base64.encodeAsString(String.format("%s:%s", userId, password))))
                .get();

        Assert.assertEquals(200, response.getStatus());

    }

    @Test
    public void DenyAllUserId() {

        final String userId = UUID.randomUUID().toString();
        final String password = UUID.randomUUID().toString();

        final Response response = client.target(
                String.format("http://localhost:%d/endpoint5/%s", dropwizardAppRule.getLocalPort(), userId))
                .request()
                .header("Authorization", String.format("Basic %s", Base64.encodeAsString(String.format("%s:%s", userId, password))))
                .get();

        Assert.assertEquals(403, response.getStatus());

    }

    @Test
    public void DenyAllInvalidUserId() {

        final String userId = UUID.randomUUID().toString();
        final String password = UUID.randomUUID().toString();

        final Response response = client.target(
                String.format("http://localhost:%d/endpoint5/%s", dropwizardAppRule.getLocalPort(), UUID.randomUUID().toString()))
                .request()
                .header("Authorization", String.format("Basic %s", Base64.encodeAsString(String.format("%s:%s", userId, password))))
                .get();

        Assert.assertEquals(403, response.getStatus());

    }

}
