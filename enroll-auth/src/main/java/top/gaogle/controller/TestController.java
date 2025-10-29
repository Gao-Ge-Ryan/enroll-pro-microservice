package top.gaogle.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理
 *
 * @author gaogle
 * @since 1.0.0
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping
    public String test() {
        System.out.println("sssssssssssssssss=====ssssssss");
        return "hello world=============";
    }

}
