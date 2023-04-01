package fastcampus.workschedulemanagementbackend.common;

import fastcampus.workschedulemanagementbackend.common.utils.AESUtil;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class RequestDecryptWrapper extends HttpServletRequestWrapper {
    private final Charset encoding;
    private String decodingBody;
    private byte[] rawData;

    public RequestDecryptWrapper(HttpServletRequest request) {
        super(request);
        String charEncoding = request.getCharacterEncoding();

        this.encoding = ObjectUtils.isEmpty(charEncoding) ? StandardCharsets.UTF_8 : Charset.forName(charEncoding);

        try {
            InputStream inputStream = request.getInputStream();
            rawData = IOUtils.toByteArray(inputStream);

            if (ObjectUtils.isEmpty(rawData)) {
                return;
            }
            AESUtil aesUtil = new AESUtil();

            this.decodingBody = aesUtil.decrypt(new String(rawData, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodingBody == null ? "".getBytes(encoding) : decodingBody.getBytes(encoding));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
