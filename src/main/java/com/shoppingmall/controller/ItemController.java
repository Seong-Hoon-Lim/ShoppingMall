package com.shoppingmall.controller;

import com.shoppingmall.dto.ItemDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {

    /* 상품 등록 페이지 */
    @GetMapping(value = "/admin/item/addItem")
    public String registerItemPage(Model model) {
        model.addAttribute("itemDTO", new ItemDTO());
        return "item/registerItem";
    }




}
