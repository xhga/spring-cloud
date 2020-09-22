package com.example.demo.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 重定向, 模拟短链接
 */
@Controller
@RequestMapping("/redirect")
public class RedirectController {



    @GetMapping("/other/{shortLink}")
    public String other(@PathVariable String shortLink) {
        System.out.println("短链接shortLink="+shortLink);
        return "redirect:" + this.getLongLink(shortLink);
    }

    String getLongLink(String shortLink) {
        String longLink = "";
        if ("1".equals(shortLink)) {
            longLink = "https://www.baidu.com";
        } else if ("2".equals(shortLink)) {
            longLink = "https://y.qq.com/download/download.html";
        }
        return longLink;
    }
}
