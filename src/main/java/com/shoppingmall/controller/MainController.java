package com.shoppingmall.controller;

import com.shoppingmall.dto.ItemSearchDTO;
import com.shoppingmall.dto.MainItemDTO;
import com.shoppingmall.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

/**
 * 메인 페이지에 상품 데이터를 보여주기 위한 컨트롤러 클래스
 */
@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping(value = "/")
    public String main(ItemSearchDTO searchDTO, Optional<Integer> page,
                       Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemDTO> items =
                itemService.getMainItemPage(searchDTO, pageable);
        model.addAttribute("items", items);
        model.addAttribute("searchDTO", searchDTO);
        model.addAttribute("maxPage", 5);
        return "main";
    }

}
