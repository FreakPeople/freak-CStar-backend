package yjh.cstar.auth.jwt

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
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import yjh.cstar.util.Logger
import java.security.Key
import java.util.*

@Component
class TokenProvider(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.token-validity-in-seconds}") tokenValidityInSeconds: Long,
) : InitializingBean {

    private val tokenValidityInMilliseconds = tokenValidityInSeconds * 1000
    private var key: Key? = null

    companion object {
        private const val MEMBER_ID = "member_id"
        private const val AUTHORITIES_KEY = "auth"
    }

    override fun afterPropertiesSet() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))
    }

    fun createToken(memberId: Long, email: String): String {
        val validity = Date(Date().time + tokenValidityInMilliseconds)

        val claims = Jwts.claims().apply {
            put(MEMBER_ID, memberId)
            put(AUTHORITIES_KEY, "")
            subject = email
        }

        return Jwts.builder()
            .addClaims(claims)
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
            Logger.info("잘못된 JWT 서명입니다.")
        } catch (e: MalformedJwtException) {
            Logger.info("잘못된 JWT 서명입니다.")
        } catch (e: ExpiredJwtException) {
            Logger.info("만료된 JWT 토큰입니다.")
        } catch (e: UnsupportedJwtException) {
            Logger.info("지원되지 않는 JWT 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            Logger.info("JWT 토큰이 잘못되었습니다.")
        }
        return false
    }

    fun getMemberId(authentication: Authentication): Long {
        val jwtToken = getJwtTokenFrom(authentication)
        return getMemberId(jwtToken)
    }

    fun getMemberId(token: String): Long {
        val claims = Jwts.parserBuilder().setSigningKey(key)
            .build().parseClaimsJws(token)
        return claims.body[MEMBER_ID].toString().toLong()
    }

    fun getJwtTokenFrom(authentication: Authentication): String {
        if (authentication.credentials is String) {
            return authentication.credentials as String
        }
        throw BaseException(BaseErrorCode.INTERNAL_SERVER_ERROR)
    }
}
