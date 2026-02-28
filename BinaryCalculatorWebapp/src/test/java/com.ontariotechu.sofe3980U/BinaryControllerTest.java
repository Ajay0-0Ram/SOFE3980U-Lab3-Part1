package com.ontariotechu.sofe3980U;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(BinaryController.class)
public class BinaryControllerTest {

    @Autowired
    private MockMvc mvc;

   
    @Test
    public void getDefault() throws Exception {
        this.mvc.perform(get("/"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("calculator"))
			.andExpect(model().attribute("operand1", ""))
			.andExpect(model().attribute("operand1Focused", false));
    }
	
	    @Test
    public void getParameter() throws Exception {
        this.mvc.perform(get("/").param("operand1","111"))
            .andExpect(status().isOk())
            .andExpect(view().name("calculator"))
			.andExpect(model().attribute("operand1", "111"))
			.andExpect(model().attribute("operand1Focused", true));
    }
	@Test
	    public void postParameter() throws Exception {
        this.mvc.perform(post("/").param("operand1","111").param("operator","+").param("operand2","111"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
			.andExpect(model().attribute("result", "1110"))
			.andExpect(model().attribute("operand1", "111"));
    }

    //added
    @Test
    public void postAddCarryGrowth() throws Exception {
        this.mvc.perform(post("/")
                .param("operand1", "1111")
                .param("operator", "+")
                .param("operand2", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "10000"))
            .andExpect(model().attribute("operand1", "1111"));
    }

    @Test
    public void postInvalidOperatorShowsError() throws Exception {
        this.mvc.perform(post("/")
                .param("operand1", "111")
                .param("operator", "^")
                .param("operand2", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("Error"));
    }

    @Test
    public void postAddDifferentLengths() throws Exception {
        this.mvc.perform(post("/")
                .param("operand1", "1")
                .param("operator", "+")
                .param("operand2", "10101"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "10110"))
            .andExpect(model().attribute("operand1", "1"));
    }

    @Test
public void postMultiply() throws Exception {
    this.mvc.perform(post("/")
            .param("operand1", "111")
            .param("operator", "*")
            .param("operand2", "101"))
        .andExpect(status().isOk())
        .andExpect(view().name("result"))
        .andExpect(model().attribute("result", "100011"));
}

    //new operators
    @Test
    public void postAnd() throws Exception {
        this.mvc.perform(post("/")
                .param("operand1", "1101")
                .param("operator", "&")
                .param("operand2", "1011"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1001"));
    }

    @Test
    public void postOr() throws Exception {
        this.mvc.perform(post("/")
                .param("operand1", "1101")
                .param("operator", "|")
                .param("operand2", "1011"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1111"));
    }




}