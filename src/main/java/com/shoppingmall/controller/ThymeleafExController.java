package com.shoppingmall.controller;

import com.shoppingmall.dto.ItemDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf/*")
public class ThymeleafExController {

    @GetMapping(value = "/ex01")
    public String thymeleafEx01(Model model) {
        model.addAttribute("data", "안녕 타임리프!");
        return "thymeleaf/thymeleafEx01";
    }

    @GetMapping(value = "/ex02")
    public String thymeleafEx02(Model model) {
        ItemDTO item = new ItemDTO();
        item.setDescription("test 셔츠입니다");
        item.setName("test 셔츠");
        item.setPrice(20000);
        item.setCreateTime(LocalDateTime.now());

        model.addAttribute("item", item);
        return "thymeleaf/thymeleafEx02";

    }

    @GetMapping(value = "/ex03")
    public String thymeleafEx03(Model model) {

        List<ItemDTO> itemList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ItemDTO item = new ItemDTO();
            item.setDescription("test 셔츠 " + i + "입니다.");
            item.setName("test 셔츠" + i);
            item.setPrice(20000 + i);
            item.setCreateTime(LocalDateTime.now());

            itemList.add(item);
        }
        model.addAttribute("items", itemList);
        return "thymeleaf/thymeleafEx03";
    }

    @GetMapping(value = "/ex04")
    public String thymeleafEx04(Model model) {

        List<ItemDTO> itemList = new ArrayList<>();

        for (int i = 0; i <= 10; i++) {
            ItemDTO item = new ItemDTO();
            item.setDescription("test 신발" + i + "입니다.");
            item.setName("test 신발" + i);
            item.setPrice(40000 + i);
            item.setCreateTime(LocalDateTime.now());

            itemList.add(item);
        }
        model.addAttribute("items", itemList);
        return "thymeleaf/thymeleafEx04";
    }


}
