package fastcampus.workschedulemanagementbackend.common;

import fastcampus.workschedulemanagementbackend.utils.AESUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

//요청 수행을 마치고 그 결과 값을 클라이언트로 전달하기 위한 reponse 객체의 output stream에 평문데이터를 쓴다.
//getOutputStream()를 오버라이딩 하여 해당 메소드가 호출되어 write() 가 실행 될때 ByteArrayOutputStream에 쓰도록 한다.
//encryptResponse() : Wrapper에 담긴 데이터를 꺼내서 암호화 하는 메소드
public class ResponseEncryptWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream output;

    public ResponseEncryptWrapper(HttpServletResponse response) {
        super(response);
        output = new ByteArrayOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {

            }

            @Override
            public void write(int b) throws IOException {
                output.write(b);
            }
        };
    }

    public byte[] encryptResponse() {
        String responseMessage = new String(output.toByteArray(), StandardCharsets.UTF_8);
        AESUtil aesUtil = new AESUtil();
        return aesUtil.encrypt(responseMessage).getBytes(StandardCharsets.UTF_8);
    }
}
