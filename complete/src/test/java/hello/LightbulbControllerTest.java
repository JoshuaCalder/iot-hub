package hello;


import hello.Devices.*;
import hello.Controllers.*;
import hello.Services.*;
import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LightbulbControllerTest {

    @Autowired
    WebApplicationContext wContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LightbulbController lightController;

    /*
    @Before
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wContext).build();

        mockMvc = MockMvcBuilders.webAppContextSetup(wContext)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();

    }
    */

    @Test
    public void validateLightbulb() {
        Hub m1 = new Hub();
        Lightbulb l1 = new Lightbulb(m1);
        //assertThat(lightController).isNotNull();
        assertThat(l1).isNotNull();
        assertEquals(l1.getStatus(), Status.OFF);
        l1.setStatus(Status.ON);
        assertEquals(l1.getStatus(), Status.ON);
    }


}