package ca.mestevens.java.server.filter;

import org.glassfish.jersey.server.model.AnnotatedMethod;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

public class DynamicRolesDynamicFeature implements DynamicFeature {

    @Override
    public void configure(final ResourceInfo resourceInfo, final FeatureContext featureContext) {

        final AnnotatedMethod method = new AnnotatedMethod(resourceInfo.getResourceMethod());

        if (method.isAnnotationPresent(DenyAll.class)) {
            featureContext.register(new DynamicRoleFilter());
            return;
        }

        final RolesAllowed rolesAllowed = method.getAnnotation(RolesAllowed.class);
        if (rolesAllowed != null) {
            featureContext.register(new DynamicRoleFilter(rolesAllowed.value()));
            return;
        }

        if (method.isAnnotationPresent(PermitAll.class)) {
            return;
        }

        final Class<?> resourceClass = resourceInfo.getResourceClass();
        if (resourceClass.isAnnotationPresent(DenyAll.class)) {
            featureContext.register(new DynamicRoleFilter());
            return;
        }

        final RolesAllowed classRolesAllowed = resourceClass.getAnnotation(RolesAllowed.class);
        if (classRolesAllowed != null) {
            featureContext.register(new DynamicRoleFilter(classRolesAllowed.value()));
        }

    }

}
