package com.example.demo;

import com.example.demo.util.PdfUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ClientApplication.class)
public class PdfTest {

    @Test
    public void test() throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", "hahaha");
        paramMap.put("author", "luck");
        //boolean pdf_page_one = PdfUtil.generatePDF("pdf_page_one", paramMap);
        //System.out.println(pdf_page_one);
    }

}
