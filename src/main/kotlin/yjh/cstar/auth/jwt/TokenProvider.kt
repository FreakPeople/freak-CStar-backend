package yjh.cstar.auth.jwt

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date

private val logger = KotlinLogging.logger {}

@Component
class TokenProvider(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.token-validity-in-seconds}") tokenValidityInSeconds: Long,
) : InitializingBean {

    private val tokenValidityInMilliseconds: Long
    private var key: Key? = null

    init {
        tokenValidityInMilliseconds = tokenValidityInSeconds * 1000
    }

    companion object {
        private const val AUTHORITIES_KEY = "auth"
    }

    override fun afterPropertiesSet() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))
    }

    fun createToken(email: String): String {
        val validity = Date(Date().time + tokenValidityInMilliseconds)

        return Jwts.builder()
            .setSubject(email)
            .claim(AUTHORITIES_KEY, "")
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact()
    }

    fun getAuthentication(token: String?): Authentication {
        val claims = Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        val authorities: Collection<GrantedAuthority> = claims[AUTHORITIES_KEY]
            .toString()
            .split(",")
            .filter { it.isNotEmpty() }
            .map { role -> SimpleGrantedAuthority(role) }

        val principal = User(claims.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: SecurityException) {
            logger.info { "잘못된 JWT 서명입니다." }
        } catch (e: MalformedJwtException) {
            logger.info { "잘못된 JWT 서명입니다." }
        } catch (e: ExpiredJwtException) {
            logger.info { "만료된 JWT 토큰입니다." }
        } catch (e: UnsupportedJwtException) {
            logger.info { "지원되지 않는 JWT 토큰입니다." }
        } catch (e: IllegalArgumentException) {
            logger.info { "JWT 토큰이 잘못되었습니다." }
        }
        return false
    }
}
