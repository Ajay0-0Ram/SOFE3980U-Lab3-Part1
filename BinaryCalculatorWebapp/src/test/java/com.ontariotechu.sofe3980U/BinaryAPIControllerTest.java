package com.ontariotechu.sofe3980U;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(BinaryAPIController.class)
public class BinaryAPIControllerTest {

    @Autowired
    private MockMvc mvc;

   
    @Test
    public void add() throws Exception {
        this.mvc.perform(get("/add").param("operand1","111").param("operand2","1010"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("10001"));
    }
	@Test
    public void add2() throws Exception {
        this.mvc.perform(get("/add_json").param("operand1","111").param("operand2","1010"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand1").value(111))
			.andExpect(MockMvcResultMatchers.jsonPath("$.operand2").value(1010))
			.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(10001))
			.andExpect(MockMvcResultMatchers.jsonPath("$.operator").value("add"));
    }

    //added
    @Test
    public void addZeroZero() throws Exception {
        this.mvc.perform(get("/add").param("operand1", "0").param("operand2", "0"))
            .andExpect(status().isOk())
            .andExpect(content().string("0"));
    }

    @Test
    public void addCarryGrowth() throws Exception {
        this.mvc.perform(get("/add").param("operand1", "1111").param("operand2", "1"))
            .andExpect(status().isOk())
            .andExpect(content().string("10000"));
    }

    @Test
    public void addJsonFields() throws Exception {
        this.mvc.perform(get("/add_json").param("operand1", "1111").param("operand2", "1"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand1").value(1111))
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand2").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(10000));
    }

    //new operators
    /*
    MULTIPLICATION
     */
    @Test
    public void mulByZero() throws Exception {
        this.mvc.perform(get("/multiply").param("operand1", "10101").param("operand2", "0"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand1").value("10101"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand2").value("0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.operator").value("multiply"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("0"));
    }

    @Test
    public void mulByOne() throws Exception {
        this.mvc.perform(get("/multiply").param("operand1", "10101").param("operand2", "1"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand1").value("10101"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand2").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.operator").value("multiply"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("10101"));
    }

    @Test
    public void mulTypical() throws Exception {
        // 111 (7) * 101 (5) = 35 -> 100011
        this.mvc.perform(get("/multiply").param("operand1", "111").param("operand2", "101"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand1").value("111"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand2").value("101"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.operator").value("multiply"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("100011"));
    }


    /*
    BITWISE
     */
    private String to2BitBinary(int x) {
        String s = Integer.toBinaryString(x);
        if (s.length() == 1) return "0" + s;
        return s;
    }

    private String toBinaryNoLeadingZeros(int x) {
        // matches your Binary normalization: "0" stays "0"
        return (x == 0) ? "0" : Integer.toBinaryString(x);
    }

    private String norm(String s) {
        // mimic Binary constructor normalization: strip leading zeros, but keep "0"
        int i = 0;
        while (i < s.length() && s.charAt(i) == '0') i++;
        String out = (i == s.length()) ? "0" : s.substring(i);
        return out;
    }

    @Test
    public void andExhaustive2BitAllPairs() throws Exception {
        for (int a = 0; a <= 3; a++) {
            for (int b = 0; b <= 3; b++) {
                String op1 = to2BitBinary(a); // "00","01","10","11"
                String op2 = to2BitBinary(b);
                String expected = toBinaryNoLeadingZeros(a & b);

                this.mvc.perform(get("/and").param("operand1", op1).param("operand2", op2))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.operand1").value(norm(op1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.operand2").value(norm(op2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.operator").value("and"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(expected));
            }
        }
    }

    @Test
    public void orExhaustive2BitAllPairs() throws Exception {
        for (int a = 0; a <= 3; a++) {
            for (int b = 0; b <= 3; b++) {
                String op1 = to2BitBinary(a);
                String op2 = to2BitBinary(b);
                String expected = toBinaryNoLeadingZeros(a | b);

                this.mvc.perform(get("/or").param("operand1", op1).param("operand2", op2))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.operand1").value(norm(op1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.operand2").value(norm(op2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.operator").value("or"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(expected));
            }
        }
    }




}