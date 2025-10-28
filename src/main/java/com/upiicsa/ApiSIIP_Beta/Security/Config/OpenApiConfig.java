package com.upiicsa.ApiSIIP_Beta.Security.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
// 1. Define la metadata de tu API
@OpenAPIDefinition(
        info = @Info(
                title = "ApiSIIP Beta - Documentación Oficial",
                version = "0.0.1",
                description = "API REST para la gestión de documentación y usuarios del sistema SIIP.",
                contact = @Contact(
                        name = "Soporte UPIICSA",
                        email = "soporte@upiicsa.edu"
                ),
                license = @License(
                        name = "Licencia Interna"
                )
        )
)
// 2. Define el esquema de seguridad (JWT Bearer)
@SecurityScheme(
        name = "bearerAuth", // 💡 Nombre que usarás para referenciar el esquema
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Ingresa el token JWT, con el prefijo 'Bearer '."
)
public class OpenApiConfig {
}
