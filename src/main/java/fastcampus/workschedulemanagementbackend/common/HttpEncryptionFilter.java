package fastcampus.workschedulemanagementbackend.common;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

//Wrapper 들을 생성하여 필터 체인에 넘긴다.
//모든 로직을 수행하고 나서 httpServletResponse의 output stream에 암호화 된 데이터를 쓴다.
@Slf4j
@WebFilter("/*")
public class HttpEncryptionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        RequestDecryptWrapper requestDecryptWrapper = new RequestDecryptWrapper(httpServletRequest);
        ResponseEncryptWrapper responseEncryptWrapper = new ResponseEncryptWrapper(httpServletResponse);

        chain.doFilter(requestDecryptWrapper, responseEncryptWrapper);

        httpServletResponse.getOutputStream().write(responseEncryptWrapper.encryptResponse());
    }
}
