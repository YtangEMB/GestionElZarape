package org.utl.dsm.rest;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import org.utl.dsm.controller.ControllerAuth;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        if (path.startsWith("Login")) {
            return;
        }

        List<String> usuarioHeaders = requestContext.getHeaders().get("usuario");
        List<String> tokenHeaders = requestContext.getHeaders().get("token");

        if (usuarioHeaders == null || tokenHeaders == null || usuarioHeaders.isEmpty() || tokenHeaders.isEmpty()) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"result\":\"error\", \"message\":\"Credenciales faltantes\"}")
                .build());
            return;
        }

        String usuario = usuarioHeaders.get(0);
        String token = tokenHeaders.get(0);

        ControllerAuth controllerAuth = new ControllerAuth();
        if (!controllerAuth.validateToken(usuario, token)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"result\":\"error\", \"message\":\"Usuario o token inválido\"}")
                .build());
        }
    }
}
