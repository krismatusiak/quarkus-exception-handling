package com.qwerty

import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path

@Path("/endpoint")
@ApplicationScoped
class Controller {

    @POST
    @Consumes("application/json")
    fun control(inputReceived: MyDto?): Uni<String> {

        return Uni.createFrom().item("Lets be simplistic.")
    }
}
