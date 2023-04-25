package com.shoppingmall.controller;

import com.shoppingmall.dto.ItemDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    /* 타임리프 th:text 문법 예제*/
    @GetMapping(value = "/ex02")
    public String thymeleafEx02(Model model) {
        ItemDTO item = new ItemDTO();
        item.setDescription("test 셔츠입니다");
        item.setItemName("test 셔츠");
        item.setPrice(20000);
//        item.setCreateTime(LocalDateTime.now());

        model.addAttribute("item", item);
        return "thymeleaf/thymeleafEx02";

    }

    /* 타임리프 반복문 th:each 문법 예제*/
    @GetMapping(value = "/ex03")
    public String thymeleafEx03(Model model) {

        List<ItemDTO> itemList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ItemDTO item = new ItemDTO();
            item.setDescription("test 셔츠 " + i + "입니다.");
            item.setItemName("test 셔츠" + i);
            item.setPrice(20000 + i);
//            item.setCreateTime(LocalDateTime.now());

            itemList.add(item);
        }
        model.addAttribute("items", itemList);
        return "thymeleaf/thymeleafEx03";
    }

    /* 타임리프 조건문 th:if, th:unless 문법 예제 */
    @GetMapping(value = "/ex04")
    public String thymeleafEx04(Model model) {

        List<ItemDTO> itemList = new ArrayList<>();

        for (int i = 0; i <= 10; i++) {
            ItemDTO item = new ItemDTO();
            item.setDescription("test 신발" + i + "입니다.");
            item.setItemName("test 신발" + i);
            item.setPrice(40000 + i);
//            item.setCreateTime(LocalDateTime.now());

            itemList.add(item);
        }
        model.addAttribute("items", itemList);
        return "thymeleaf/thymeleafEx04";
    }

    /* 타임리프 반복문 th:switch, th:case 문법 예제 */
    @GetMapping(value = "/ex04-01")
    public String thymeleafEx04_01(Model model) {

        List<ItemDTO> itemList = new ArrayList<>();

        for (int i = 0; i <= 10; i++) {
            ItemDTO item = new ItemDTO();
            item.setDescription("test 신발" + i + "입니다.");
            item.setItemName("test 신발" + i);
            item.setPrice(40000 + i);
//            item.setCreateTime(LocalDateTime.now());

            itemList.add(item);
        }
        model.addAttribute("items", itemList);
        return "thymeleaf/thymeleafEx04-01";
    }

    /* 타임리프 th:href 문법 예제 */
    @GetMapping(value = "/ex05")
    public String thymeleafEx05() {
        return "thymeleaf/thymeleafEx05";
    }

    /* 타임리프 param 문법을 활용 파라미터 데이터 전달 예제 */
    @GetMapping(value = "/ex06")
    public String thymeleafEx06(String param1, String param2, Model model) {
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleaf/thymeleafEx06";
    }

    /* 타임리프 페이지 레이아웃 활용 예제 */
    @GetMapping(value = "/ex07")
    public String thymeleafEx07() {
        return "thymeleaf/thymeleafEx07";
    }


}
