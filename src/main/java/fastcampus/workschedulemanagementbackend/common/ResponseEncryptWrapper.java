package fastcampus.workschedulemanagementbackend.common;

import fastcampus.workschedulemanagementbackend.common.utils.AESUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
