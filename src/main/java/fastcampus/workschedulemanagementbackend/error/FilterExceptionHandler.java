package fastcampus.workschedulemanagementbackend.error;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class FilterExceptionHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException e){
            //토큰의 유효기간 만료
            setErrorResponse(response, ErrorCode.JWT_EXPIRED);
        }catch (JwtException | IllegalArgumentException e){
            // 이상한 토큰
            setErrorResponse(response, ErrorCode.JWT_WRONG);
        }
    }

    private void setErrorResponse(
            HttpServletResponse response,
            ErrorCode errorCode
    ) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(errorCode.getMessage());
    }

}
