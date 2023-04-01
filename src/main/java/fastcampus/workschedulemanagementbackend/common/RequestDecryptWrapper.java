package fastcampus.workschedulemanagementbackend.common;

import fastcampus.workschedulemanagementbackend.utils.AESUtil;
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

//HttpServletRequest를 생성자로 주입받아서 바디 데이터를 꺼내 복호화 한 후 멤버변수에 저장한다.
//getInputStream()를 오버라이딩하여 복호화된 decodingBody를 가지고 input stream을 생성하여 리턴 하도록 한다.
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
