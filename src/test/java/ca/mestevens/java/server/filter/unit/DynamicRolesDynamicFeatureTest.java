package ca.mestevens.java.server.filter.unit;

import ca.mestevens.java.UnitTest;
import ca.mestevens.java.server.filter.DynamicRoleFilter;
import ca.mestevens.java.server.filter.DynamicRolesDynamicFeature;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

@Category(UnitTest.class)
public class DynamicRolesDynamicFeatureTest {

    private DynamicRolesDynamicFeature dynamicRolesDynamicFeature;

    private ResourceInfo resourceInfo;

    private FeatureContext featureContext;

    @Before
    public void setUp() {
        dynamicRolesDynamicFeature = new DynamicRolesDynamicFeature();
        resourceInfo = Mockito.mock(ResourceInfo.class);
        featureContext = Mockito.mock(FeatureContext.class);
    }

    @After
    public void tearDown() {
        featureContext = null;
        resourceInfo = null;
        dynamicRolesDynamicFeature = null;
    }

    @Test
    @SneakyThrows
    public void testDenyAllAnnotationOnMethod() {

        Mockito.when(resourceInfo.getResourceMethod())
                .thenReturn(DenyAllMethod.class.getMethod("method"));

        dynamicRolesDynamicFeature.configure(resourceInfo, featureContext);

        Mockito.verify(resourceInfo).getResourceMethod();
        Mockito.verify(featureContext).register(Mockito.any(DynamicRoleFilter.class));
        Mockito.verifyNoMoreInteractions(resourceInfo);
        Mockito.verifyNoMoreInteractions(featureContext);

    }

    @Test
    @SneakyThrows
    public void testDenyAllAnnotationOnClass() {

        Mockito.when(resourceInfo.getResourceMethod())
                .thenReturn(DenyAllClass.class.getMethod("method"));
        Mockito.when(resourceInfo.getResourceClass())
                .thenAnswer(i -> DenyAllClass.class);

        dynamicRolesDynamicFeature.configure(resourceInfo, featureContext);

        Mockito.verify(resourceInfo).getResourceMethod();
        Mockito.verify(resourceInfo).getResourceClass();
        Mockito.verify(featureContext).register(Mockito.any(DynamicRoleFilter.class));
        Mockito.verifyNoMoreInteractions(resourceInfo);
        Mockito.verifyNoMoreInteractions(featureContext);

    }

    @Test
    @SneakyThrows
    public void testRolesAllowedAnnotationOnMethod() {

        Mockito.when(resourceInfo.getResourceMethod())
                .thenReturn(RolesAllowedMethod.class.getMethod("method"));

        dynamicRolesDynamicFeature.configure(resourceInfo, featureContext);

        Mockito.verify(resourceInfo).getResourceMethod();
        Mockito.verify(featureContext).register(Mockito.any(DynamicRoleFilter.class));
        Mockito.verifyNoMoreInteractions(resourceInfo);
        Mockito.verifyNoMoreInteractions(featureContext);

    }

    @Test
    @SneakyThrows
    public void testRolesAllowedAnnotationOnClass() {

        Mockito.when(resourceInfo.getResourceMethod())
                .thenReturn(RolesAllowedClass.class.getMethod("method"));
        Mockito.when(resourceInfo.getResourceClass())
                .thenAnswer(i -> RolesAllowedClass.class);

        dynamicRolesDynamicFeature.configure(resourceInfo, featureContext);

        Mockito.verify(resourceInfo).getResourceMethod();
        Mockito.verify(resourceInfo).getResourceClass();
        Mockito.verify(featureContext).register(Mockito.any(DynamicRoleFilter.class));
        Mockito.verifyNoMoreInteractions(resourceInfo);
        Mockito.verifyNoMoreInteractions(featureContext);

    }

    @Test
    @SneakyThrows
    public void testPermitAllAnnotationOnMethod() {

        Mockito.when(resourceInfo.getResourceMethod())
                .thenReturn(PermitAllMethod.class.getMethod("method"));

        dynamicRolesDynamicFeature.configure(resourceInfo, featureContext);

        Mockito.verify(resourceInfo).getResourceMethod();
        Mockito.verifyNoMoreInteractions(resourceInfo);
        Mockito.verifyNoMoreInteractions(featureContext);

    }

    @Test
    @SneakyThrows
    public void testPermitAllAnnotationOnClass() {

        Mockito.when(resourceInfo.getResourceMethod())
                .thenReturn(PermitAllClass.class.getMethod("method"));
        Mockito.when(resourceInfo.getResourceClass())
                .thenAnswer(i -> PermitAllClass.class);

        dynamicRolesDynamicFeature.configure(resourceInfo, featureContext);

        Mockito.verify(resourceInfo).getResourceMethod();
        Mockito.verify(resourceInfo).getResourceClass();
        Mockito.verifyNoMoreInteractions(resourceInfo);
        Mockito.verifyNoMoreInteractions(featureContext);

    }

    @Test
    @SneakyThrows
    public void testNoAnnotation() {

        Mockito.when(resourceInfo.getResourceMethod())
                .thenReturn(NoAnnotationsClass.class.getMethod("method"));
        Mockito.when(resourceInfo.getResourceClass())
                .thenAnswer(i -> PermitAllClass.class);

        dynamicRolesDynamicFeature.configure(resourceInfo, featureContext);

        Mockito.verify(resourceInfo).getResourceMethod();
        Mockito.verify(resourceInfo).getResourceClass();
        Mockito.verifyNoMoreInteractions(resourceInfo);
        Mockito.verifyNoMoreInteractions(featureContext);

    }

    @Test
    @SneakyThrows
    public void testAllAnnotations() {

        Mockito.when(resourceInfo.getResourceMethod())
                .thenReturn(AllAnnotationsClass.class.getMethod("method"));

        dynamicRolesDynamicFeature.configure(resourceInfo, featureContext);

        Mockito.verify(resourceInfo).getResourceMethod();
        Mockito.verify(featureContext).register(Mockito.any(DynamicRoleFilter.class));
        Mockito.verifyNoMoreInteractions(resourceInfo);
        Mockito.verifyNoMoreInteractions(featureContext);

    }

    private static class DenyAllMethod {

        @DenyAll
        @SuppressWarnings("all")
        public void method() {}

    }

    @DenyAll
    private static class DenyAllClass {

        @SuppressWarnings("all")
        public void method() {}

    }

    private static class RolesAllowedMethod {

        @RolesAllowed("")
        @SuppressWarnings("all")
        public void method() {}

    }

    private static class PermitAllMethod {

        @PermitAll
        @SuppressWarnings("all")
        public void method() {}

    }

    @RolesAllowed("")
    private static class RolesAllowedClass {

        @SuppressWarnings("all")
        public void method() {}

    }

    @PermitAll
    private static class PermitAllClass {

        @SuppressWarnings("all")
        public void method() {}

    }

    private static class NoAnnotationsClass {

        @SuppressWarnings("all")
        public void method() {}

    }

    @DenyAll
    @PermitAll
    @RolesAllowed("")
    private static class AllAnnotationsClass {

        @DenyAll
        @PermitAll
        @RolesAllowed("")
        @SuppressWarnings("all")
        public void method() {}

    }

}
