package com.skiply.system.student.api.documentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Skiply system's student management service - OpenAPI 3.0 documentation",
                description = """
                        At the moment, this service's only exposes API for student registration in
                        the Skiply system. And it will possibly be extended further for other student
                        management activities.
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