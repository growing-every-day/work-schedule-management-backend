package fastcampus.workschedulemanagementbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fastcampus.workschedulemanagementbackend.controller.request.UserJoinRequest;
import fastcampus.workschedulemanagementbackend.domain.UserAccount;
import fastcampus.workschedulemanagementbackend.exception.wsAppException;
import fastcampus.workschedulemanagementbackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void 회원가입() throws Exception {
        String id = "id";
        String password = "password";
        String email = "example@gmail.com";
        String name = "name";

        when(userService.join(id, password, email, name)).thenReturn(mock(UserAccount.class));


        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                // TODO : add request body
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(id, password,email,name)))
        ).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void 회원가입시_이미_회원가입된_userName으로_회원가입을_하는경우_에러반환() throws Exception{
        String id = "id";
        String password = "password";
        String email = "example@gmail.com";
        String name = "name";


        when(userService.join(id, password, email, name)).thenThrow(new wsAppException());



        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO : add request body
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(id, password,email,name)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }
}
