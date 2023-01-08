package com.skiply.system.receiptEntity.api.documentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Skiply system's retrieve receiptEntity service - OpenAPI 3.0 documentation",
                description = """
                        Exposes APIs to retrieve receiptEntity details to generate receipts for the fee paid for a student in
                        the Skiply system.
                        """,
                contact = @Contact(
                        name = "Karthikaiselvan R",
                        url = "https://www.linkedin.com/in/karthikairam",
                        email = "karthikairam@gmail.com"
                ),
                license = @License(
                        name = "MIT Licence",
                        url = "")),
        servers = @Server(url = "http://localhost:8081")
)
public class OpenApiConfiguration {
}