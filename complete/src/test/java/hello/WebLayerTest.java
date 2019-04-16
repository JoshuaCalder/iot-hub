package hello;

import hello.Devices.*;
import hello.Controllers.*;
import hello.Services.*;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.Mockito;
import org.mockito.InjectMocks;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import org.mockito.Mock;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@WebMvcTest(HubController.class)
//@WithMockUser
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HubService s;

    @Test
    public void shouldRedirectToHubAfterAdding() throws Exception {
        this.mockMvc.perform(get("/lightbulb/add")).andExpect(redirectedUrl("/hub"));
        this.mockMvc.perform(get("/camera/add")).andExpect(redirectedUrl("/hub"));
        this.mockMvc.perform(get("/smartplug/add")).andExpect(redirectedUrl("/hub"));
        this.mockMvc.perform(get("/thermostat/add")).andExpect(redirectedUrl("/hub"));
    }

    @Test
    public void deviceControlDirectTest() throws Exception {
        this.mockMvc.perform(get("/hub")).andExpect(content().string(containsString("Devices")));
    }

    @Test
    public void adminSeeStatus() throws Exception {
        Hub h = new Hub();
        Camera c = new Camera(h);
        c.setRecordingStatus(true);
        h.register(c);
        s.addCamera();
        //this.mockMvc.perform(get("/hub")).andExpect(content().string(containsString("ON")));
    }

    @Test
    public void doLogsShow() throws Exception {
        this.mockMvc.perform(get("/hub")).andExpect(content().string(containsString("Log")));
    }
}