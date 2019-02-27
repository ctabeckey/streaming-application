package org.nanocontext.streamingapplication;

import org.nanocontext.streamingapplication.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Tests for the StreamingController and its dependant classes.
 *
 */
@SpringBootTest(classes = {Application.class})
public class StreamingControllerIS extends AbstractTestNGSpringContextTests {
    /** a logger instance */
    private final static Logger logger = LoggerFactory.getLogger(StreamingController.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeClass
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Simple test that the context loads and that the MockMVC is available
     * @throws Exception
     */
    @Test
    public void contextLoads() throws Exception {
        Assert.assertNotNull(mockMvc);
    }

    @Test
    public void testPostHeadGetDelete() throws Exception {
        logger.debug("posting resource");

        MvcResult postResult = mockMvc.perform(
                    post("/")
                            .content(new byte[]{0,1,2,3,4,5,6,7,8,9})
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .header("xxx-description", "The Description"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.identifier").exists())
                .andReturn();
        logger.debug("resource posted, parsing content");

        MockHttpServletResponse response = postResult.getResponse();
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, Object> parsed = parser.parseMap(new String(response.getContentAsByteArray()));

        String identifier = (String)parsed.get("identifier");
        Assert.assertNotNull(identifier);
        logger.debug("resource posted, identifier is '" + identifier + "'.");

        mockMvc.perform(head("/" + identifier))
                .andExpect(status().isOk())
                .andExpect(header().exists("Content-type"))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(header().exists("Content-length"))
                .andExpect(header().longValue("Content-length", 10L))
                .andExpect(header().stringValues("xxx-description", "The Description"));

        mockMvc.perform(get("/" + identifier))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));

        mockMvc.perform(delete("/" + identifier))
                .andExpect(status().isOk());

        try {
            mockMvc.perform(get("/" + identifier))
                    .andExpect(status().isNotFound());
        }
        catch (NestedServletException nsX) {
            Assert.assertTrue(nsX.getCause() instanceof ResourceNotFoundException);
        }
    }
}
