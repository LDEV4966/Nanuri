package com.example.nanuri.controller.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// @WebMvcTest : 여러 스프링 테스트 어노테이션 중, Web(Spring MVC)에 집중할 수 있는 어노테이션
// 선언할 경우 @Controller, @ControllerAdvice 등을 사용가능
// 단, @Service, @Component, @Repository 등은 사용할 수 없다. 여기서는 컨트롤러만 사용하기 때문
@ExtendWith(SpringExtension.class) // 테스트를 진행할 때 JUnit에 내장된 실행자 외에 다른 실행자를 실행
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {

    @Autowired //스프링일 관리하는 Bean을 주입 받습니다.
    private MockMvc mvc;

    @Test
    public void hello가_리턴퇸다() throws Exception{
        String hello = "hello";
//        mvc.perform(get("/hello")) // MocMvc 를 통해 /hello 주소로 HTTP GET 요청을 합니다.
//                .andExpect(status().isOk()); // HTTP Header의 status 결과를 검증. ex) 200(OK),404,500 등

    }

}
