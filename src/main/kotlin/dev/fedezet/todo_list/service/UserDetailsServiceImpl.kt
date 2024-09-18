package dev.fedezet.todo_list.service

import dev.fedezet.todo_list.controller.dto.AuthLoginRequest
import dev.fedezet.todo_list.controller.dto.AuthRegisterRequest
import dev.fedezet.todo_list.controller.dto.AuthResponse
import dev.fedezet.todo_list.persistence.entity.UserEntity
import dev.fedezet.todo_list.persistence.repository.UserRepository
import dev.fedezet.todo_list.util.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

@Service
class UserDetailsServiceImpl(
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val jwtUtils: JwtUtils,
    @Autowired
    val passwordEncoder: PasswordEncoder
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity: Optional<UserEntity> = userRepository.findUserByEmail(username)

        val authorityList: List<SimpleGrantedAuthority> = ArrayList()

        return User(
            userEntity.get().email,
            userEntity.get().password,
            userEntity.get().isEnabled(),
            userEntity.get().isAccountNonExpired(),
            userEntity.get().isCredentialsNonExpired(),
            userEntity.get().isAccountNonLocked(), authorityList
        )
    }

    fun authenticate(username: String, password: String): Authentication {
        val userDetails: UserDetails = this.loadUserByUsername(username)

        if(!passwordEncoder.matches(password, userDetails.password)) {
            throw BadCredentialsException("Invalid password")
        }

        return UsernamePasswordAuthenticationToken(username, userDetails.password, null)
    }

    fun loginUser(authLoginRequest: AuthLoginRequest): AuthResponse {
        val username: String = authLoginRequest.email
        val password: String = authLoginRequest.password

        val authentication: Authentication = this.authenticate(username, password)

        SecurityContextHolder.getContext().authentication = authentication

        val accessToken = jwtUtils.createToken(authentication)

        return AuthResponse(accessToken)
    }

    fun registerUser(authRegisterRequest: AuthRegisterRequest): AuthResponse {
        val name: String = authRegisterRequest.name
        val email: String = authRegisterRequest.email
        val password: String = authRegisterRequest.password

        val userEntity = UserEntity(
            name = name,
            email = email,
            password = passwordEncoder.encode(password)
        )

        val userCreated: UserEntity = userRepository.save(userEntity)

        val authentication: Authentication = UsernamePasswordAuthenticationToken(userCreated.email, userCreated.password, null)

        val accessToken = jwtUtils.createToken(authentication)

        return AuthResponse(accessToken)
    }
}