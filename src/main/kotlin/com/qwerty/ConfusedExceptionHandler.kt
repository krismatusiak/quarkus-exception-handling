package com.qwerty

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.quarkus.logging.Log
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.WebApplicationException
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

@ApplicationScoped
class ConfusedExceptionHandler {

    /**
     * This would be a fine situation
     * perhaps more Json exceptions should be able to be caught like this,
     * the most abstract ones perhaps?
     * JsonMappingException is a parent of MismatchedInputException, so perhaps thats a better candidate?
     */
    @ServerExceptionMapper
    fun thisIsAnExampleOfHowItDoesNOTWork(exception: JsonMappingException): Uni<RestResponse<String>> {
        Log.error(exception.message)

        return Uni.createFrom().item(
            ResponseBuilder.ok(exception.message!!)
                .status(RestResponse.Status.OK)
                .build()
        )
    }

    @ServerExceptionMapper
    fun thisIsAnExampleOfHowItCouldWork(exception: MismatchedInputException): Uni<RestResponse<String>> {
        Log.error(exception.message)

        return Uni.createFrom().item(
            ResponseBuilder.ok(exception.message!!)
                .status(RestResponse.Status.OK)
                .build()
        )
    }

//    @ServerExceptionMapper
//    fun thisDoesNotWork_ItsTooBroad_AndBreaksOtherBehaviour(exception: WebApplicationException): Uni<RestResponse<String>> {
//        Log.error(exception.message)
//
//        return Uni.createFrom().item(
//            ResponseBuilder.ok(exception.message!!)
//                .status(RestResponse.Status.OK)
//                .build()
//        )
//    }

    /**
     * The failing test gives us the following trace
     *
     * 2024-09-06 11:43:16,637 DEBUG [org.jbo.res.rea.ser.han.RequestDeserializeHandler] (vert.x-eventloop-thread-0) Error occurred during deserialization of input: jakarta.ws.rs.WebApplicationException: HTTP 400 Bad Request
     * 	at io.quarkus.resteasy.reactive.jackson.runtime.serialisers.ServerJacksonMessageBodyReader.readFrom(ServerJacksonMessageBodyReader.java:60)
     * 	...
     * Caused by: com.fasterxml.jackson.core.JsonParseException: Unexpected character ('c' (code 99)): was expecting double-quote to start field name
     *  at [Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); line: 1, column: 2]
     *  ...
     *
     *
     *
     *
     *
     *  And from what I can see:
     *  - catching WebApplicationException is not productive
     *    (it is a broad class and catching it like this can break other behaviour, as shown,
     *     even when some filtering + rethrowing is done)
     *  - catching JsonParseException is not possible anymore (it was caught and consumed by the RequestDeserializerHandler, I think)
     */
}
