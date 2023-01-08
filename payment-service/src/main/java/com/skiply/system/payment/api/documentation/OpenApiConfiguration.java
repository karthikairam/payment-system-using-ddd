package com.skiply.system.payment.api.documentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Skiply system's payment service - OpenAPI 3.0 documentation",
                description = """
                        This service exposed endpoint to collect the payment and process it.
                        """,
                contact = @Contact(
                        name = "Karthikaiselvan R",
                        url = "https://www.linkedin.com/in/karthikairam",
                        email = "karthikairam@gmail.com"
                ),
                license = @License(
                        name = "MIT Licence",
                        url = "")),
        servers = @Server(url = "http://localhost:8082")
)
public class OpenApiConfiguration {
}