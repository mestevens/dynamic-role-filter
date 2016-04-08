# dynamic-role-filter

## Description
A DynamicFeature and filter to allow the @RolesAllowed annotation to accept substitutions (similar to how you can get path params from the @Path annotation).

## Dependency Information
```
<dependency>
    <groupId>ca.mestevens.java</groupId>
    <artifactId>dynamic-role-filter</artifactId>
    <version>1.0</version>
</dependency>
```

## Usage
The example shown below will be assuming that you are using [Dropwizard](dropwizard.github.io/dropwizard) framework.

The only thing that needs to be done in order to use this, is to change the following line in your Application class (assuming you've got auth set-up already) from:
```
environment.jersey().register(RolesAllowedDynamicFeature.class);
```
to:
```
environment.jersey().register(DynamicRolesAllowedDynamicFeature.class);
```
and you're all set to go!

## Example
This lets you use replacement in your `@RolesAllowed` annotations. For example I have the following resource endpoint:
```
@GET
@Path("/users/{userId}")
public Response getUser() {
    return Response.ok().build();
}
```
which I want to return a 200 if you're the appropriate user accessing it. With the old `RolesAllowedDynamicFeature` you would have to do the following:
```
@GET
@Path("/users/{userId}")
@RolesAllowed("users:read")
public Response getUser(@Auth final User user,
                        @PathParam("userId") final String userId) {
    if (user.getName().equals(userId)) {
        return Response.ok().build();
    }
    return Response.status(403).build();
}
```
However, with the `DynamicRolesAllowedDynamicFeature` all you would need to do is:
```
@GET
@Path("/users/{userId}")
@RolesAllowed("users:{userId}:read")
public Response getUser() {
    return Response.ok().build();
}
```
Of course, you'll have to update the permissions you assign to a user appropriately. In the first example every user (who you want to have access to that endpoint) would need the broad permission of `user:read`. In the second example, each user would need more specific permissions `user:<userId>:read` where `<userId>` is the userId of the new user.