package com.qwerty

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@QuarkusTest
class ControllerTest {

    @ParameterizedTest
    @MethodSource("provideSupportedBody")
    fun postWithSupportedBody(body: String) {
        given()
            .body(body)
            .contentType("application/json")
        .When()
            .post("/endpoint")
        .then()
            .statusCode(200)
    }

    @Test
    fun thisOneCausesMismatchedInputException_BUT_WeCanCustomizeIt() {
        given()
            .body("\"not_required\":\"doesnt have to be here\"}")
            .contentType("application/json")
        .When()
            .post("/endpoint")
        .then()
            .statusCode(200)
    }

    @Test
    fun malformedBody_gives400_AND_WeCannotCustomizeIt() {
        given()
            .body("{required:no quotes scare me}")
            .contentType("application/json")
        .When()
            .post("/endpoint")
        .then()
//          let say that, for whatever reason, I only want to log and return 200:
            .statusCode(200)
    }

    @Test
    fun ifWeCatchWebApplicationException_ThisTestWillFail_ThereAreOtherCasesWhenThisWillHappen() {
        given()
        .When()
            .get("/endpoint")
        .then()
            .statusCode(405)
    }

    companion object {
        @JvmStatic
        fun provideSupportedBody() = listOf(
            arrayOf("{\"required\":\"must be present in the body\", \"not_required\":\"doesnt have to be here\"}"),
            arrayOf("{\"required\":\"must be present in the body\", \"not_required\":null}"),
            arrayOf("{\"required\":\"must be present in the body\""),
        )
    }
}

fun RequestSpecification.When(): RequestSpecification {
    return this.`when`()
}
