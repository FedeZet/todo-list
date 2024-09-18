package dev.fedezet.todo_list.util

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.Date
import java.util.UUID

@Component
class JwtUtils {
    @Value("\${security.jwt.key.private}")
    lateinit var privateKey: String

    @Value("\${security.jwt.user.generator}")
    lateinit var userGenerator: String

    fun createToken(authentication: Authentication): String {
        val algorithm: Algorithm = Algorithm.HMAC256(this.privateKey)

        val username: String = authentication.principal.toString()

        val jwtToken: String = JWT.create()
            .withIssuer(this.userGenerator)
            .withSubject(username)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + 1800000))
            .withJWTId(UUID.randomUUID().toString())
            .withNotBefore(Date(System.currentTimeMillis()))
            .sign(algorithm)

        return jwtToken
    }

    fun validateToken(token: String): DecodedJWT {
        try {
            val algorithm: Algorithm = Algorithm.HMAC256(this.privateKey)

            val verifier: JWTVerifier = JWT.require(algorithm)
                .withIssuer(this.userGenerator)
                .build()

            val decodedJWT: DecodedJWT = verifier.verify(token)
            return decodedJWT
        } catch (exception: JWTVerificationException) {
            throw exception
        }
    }

    fun extractUsername(decodedJWT: DecodedJWT): String {
        return decodedJWT.subject
    }
}