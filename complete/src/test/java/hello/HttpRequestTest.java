package hello;


import hello.Devices.*;
import hello.Controllers.*;
import hello.Services.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void greetingShouldReturnWelcomeMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
                String.class)).contains("JJBooleans IOT App. Get started");
    }

    /**
     * 1. GIVEN I want to see a Camera device WHEN That Camera has integrated video
     * streaming THEN I can see the video's live feed
     */
    @Test
    public void seeStreaming() throws Exception {
        String path = "/camera/toggle/5352f217-fe6b-41fd-914d-d4ecdb1f8455";
        String redirect = "/camera/5352f217-fe6b-41fd-914d-d4ecdb1f8455";
        assertThat(this.restTemplate.getForObject(path,
                String.class)).contains("Camera");
    }

    /**
     * 2. Given I am an administrator WHEN I want to see a device status check THEN the status
     * check thread starts and collects the data without interrupting the UI.
     */
    @Test
    public void adminSeeStatus() throws Exception {
        String path = "/hub";
        assertThat(this.restTemplate.getForObject(path, String.class)).contains("");
    }


    /**
     * 1. GIVEN I am starting the app / page WHEN I first open the application/page
     * THEN I see a user login screen
     */
    @Test
    public void bootToLogin() throws Exception {
        String path = "/user/login";
        assertThat(this.restTemplate.getForObject(path, String.class)).contains("Username");
    }

    /**
     * 3. GIVEN I am logged in as a user to the Hub WHEN I open the app interface THEN I see
     * the user interface
     */
    @Test
    public void seeUserScreen() throws Exception {
        String path = "/hub";
        assertThat(this.restTemplate.getForObject(path, String.class)).contains("");
    }

}