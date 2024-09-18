package dev.fedezet.todo_list.filter

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.databind.ObjectMapper
import dev.fedezet.todo_list.util.JwtUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtTokenValidator(@Autowired val jwtUtils: JwtUtils): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            var jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION)

            if(jwtToken != null) {
                jwtToken = jwtToken.substring(7)

                val decodedJWT: DecodedJWT = jwtUtils.validateToken(jwtToken)

                val username: String = jwtUtils.extractUsername(decodedJWT)

                val context: SecurityContext = SecurityContextHolder.getContext()
                val authentication: Authentication = UsernamePasswordAuthenticationToken(username, null, null)
                context.authentication = authentication
                SecurityContextHolder.setContext(context)
            }
            filterChain.doFilter(request, response)
        } catch (e: JWTVerificationException) {
            val objectMapper = ObjectMapper()

            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            val errorResponse = mapOf("Invalid token" to e.message)
            response.writer.write(objectMapper.writeValueAsString(errorResponse))
        }
    }
}