package com.shoppingmall.controller;

import com.shoppingmall.dto.ItemDTO;
import com.shoppingmall.dto.ItemSearchDTO;
import com.shoppingmall.entity.Item;
import com.shoppingmall.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /* 상품 등록 페이지 */
    @GetMapping(value = "/admin/item/register")
    public String registerItemPage(Model model) {
        model.addAttribute("itemDTO", new ItemDTO());
        return "item/registerItem";
    }

    /* 상품 등록 처리 */
    @PostMapping(value = "/admin/item/register")
    public String registerItemProcess(@Valid ItemDTO itemDTO,
                                      BindingResult bindingResult,
                                      Model model,
                                      @RequestParam("itemImgFile")List<MultipartFile> imgFileList) {

        //상품 등록 시 필수 값이 없다면 다시 상품 등록 페이지로 전환
        if (bindingResult.hasErrors()) {
            return "item/registerItem";
        }

        /*
         상품 등록 시 첫번째 이미지가 없다면 에러 메시지와 함께 상품 등록 페이지로 전환
         상품의 첫 이미지는 메인 페이지에서 보여줄 상품 이미지로 사용하기 위해 필수 값 지정.
         */
        else if (imgFileList.get(0).isEmpty() && itemDTO.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/registerItem";
        }

        /*
         상품 저장의 서비스 로직을 호출. 매개변수로 상품 정보와 상품 이미지 정보를
         담고 있는 itemFileList 를 넘겨줌
         */
        try {
            itemService.saveItem(itemDTO, imgFileList);
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/registerItem";
        }
        return "redirect:/";


    }

    /* 상품 상세 페이지(수정 폼) */
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemInfoPage(@PathVariable("itemId") Long itemId, Model model) {

        try {
            ItemDTO itemDTO = itemService.findItemInfo(itemId);
            //조회한 상품 데이터를 model 에 담아서 view 로 전달
            model.addAttribute("itemDTO", itemDTO);
        }
        //상품 엔티티가 존재하지 않는 경우 에러메시지를 담아서 상품 등록 페이지로 이동
        catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemDTO", new ItemDTO());
            return "item/registerItem";
        }
        return "item/registerItem";
    }

    /* 상품 업데이트 처리 */
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdateProcess(@Valid ItemDTO itemDTO, BindingResult bindingResult,
                                    @RequestParam("itemImgFile") List<MultipartFile> imgFileList,
                                    Model model) {

        //상품 등록 시 필수 값이 없다면 다시 상품 등록 페이지로 전환
        if (bindingResult.hasErrors()) {
            return "item/registerItem";
        }
        /*
         상품 등록 시 첫번째 이미지가 없다면 에러 메시지와 함께 상품 등록 페이지로 전환
         상품의 첫 이미지는 메인 페이지에서 보여줄 상품 이미지로 사용하기 위해 필수 값 지정.
         */
        else if (imgFileList.get(0).isEmpty() && itemDTO.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/registerItem";
        }

        /*
         상품 수정 로직 호출. 매개변수로 상품 정보와 상품 이미지 정보를
         담고 있는 itemFileList 를 넘겨줌.
         */
        try {
            itemService.updateItem(itemDTO, imgFileList);
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정중 에러가 발생하였습니다.");
            return "item/registerItem";
        }
        return "redirect:/";

    }

    /*
     상품 관리 화면 이동 및 조회한 상품 데이터를 화면에 전달
     한 페이지당 총 5개의 상품만 보여주도록 설정

     value 에 상품 관리 화면 진입시 URL에 페이지 번호가 없는 경우와
     페이지 번호가 있는 경우 2가지를 매핑
     */
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManagePage(ItemSearchDTO searchDTO,
                                 @PathVariable("page")Optional<Integer> page, Model model) {

        /*
         페이징을 위해서 PageRequest.of 메소드를 통해 Pageable 객체를 생성함.
         첫 번째 파라미터로는 조호할 페이지 번호, 두 번째 파라미터로는 한 번에 가지고 올
         데이터 수를 넣어줌.
         URL 경로에 페이지 번호가 있으면 해당 페이지를 조회하도록 세팅하고, 페이지 번호가
         없으면 0페이지를 조회하도록 함.
         */
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<Item> items =
                //조회 조건과 페이징 정보를 파라미터로 넘겨서 Page<Item> 객체를 반환 받음.
                itemService.getAdminItemPage(searchDTO, pageable);
        //조회한 상품 데이터 및 페이징 정보를 뷰에 전달함.
        model.addAttribute("items", items);
        //페이지 전환시 기존 검색 조건을 유지한 채 이동할 수 있도록 뷰에 다시 전달함.
        model.addAttribute("searchDTO", searchDTO);
        //상품 관리 메뉴 하단에 보여줄 페이지 번호의 최대 개수. 최대 5개의 이동할 페이지 번호만 보여줌.
        model.addAttribute("maxPage", 5);

        return "item/manageItem";

    }




}
