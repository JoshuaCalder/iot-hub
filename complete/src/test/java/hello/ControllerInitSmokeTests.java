/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hello;

import hello.Devices.*;
import hello.Controllers.*;
import hello.Services.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

//@WebMvcTest(controllers = LightbulbController.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerInitSmokeTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LightbulbController lightController;

    @Autowired
    private SmartPlugController plugController;

    @Autowired
    private CameraController cameraController;

    @Autowired
    private ThermostatController thermostatController;

    @Autowired
    private HubController hubController;

    @Test
    public void contexLoads() throws Exception {
        assertThat(lightController).isNotNull();
        assertThat(plugController).isNotNull();
        assertThat(cameraController).isNotNull();
        assertThat(thermostatController).isNotNull();
        assertThat(hubController).isNotNull();
    }
}