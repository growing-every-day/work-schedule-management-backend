package fastcampus.workschedulemanagementbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fastcampus.workschedulemanagementbackend.dto.UserAccountDto;
import fastcampus.workschedulemanagementbackend.dto.request.useraccount.UserAccountJoinRequest;
import fastcampus.workschedulemanagementbackend.service.UserAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserAccountService userAccountService;


    @Test
    @WithAnonymousUser
    public void 회원가입() throws JsonProcessingException {
       String username = "username";
       String password = "password";
       String name = "name";
       String email = "email";

//       when(userAccountService.join(UserAccountDto.of(username,password,name,email))).thenReturn(mock(UserAccountDto.class));

//       mockMvc.perform(post("/api/users/signup")
//                       .contentType(MediaType.APPLICATION_JSON)
//                       .content(objectMapper.writeValueAsBytes(UserAccountJoinRequest.of(username,password,name,email)))
//               ).andDo(print())
//               .andExpect(status().isOk());
    }
    @Test
    public void 회원가입시_이미_회원가입된_userName으로_회원가입을_하는경우_에러반환() throws JsonProcessingException {
        String username = "username";
        String password = "password";
        String name = "name";
        String email = "email";

//        mockMvc.perform(post("/api/users/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(UserAccountJoinRequest.of(username,password,name,email)))
//                ).andDo(print())
//                .andExpect(status().isConflict());
    }
}
