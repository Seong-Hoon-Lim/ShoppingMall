package com.shoppingmall.service;

import com.shoppingmall.entity.ItemImg;
import com.shoppingmall.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

/**
 * 상품의 이미지를 업로드 하고, 상품 이미지 정보를 저장하는
 * service 클래스
 */
@Service
@RequiredArgsConstructor
@Transactional
@Log
public class ItemImgService {

    //프로퍼티에 설정한 itemImgPath 의 값을 불러와서 변수에 저장
    @Value("${itemImgPath}")
    private String itemImgPath;

    private final ItemImgRepository imgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile imgFile) throws Exception {
        //업로드 했던 상품 이미지 파일의 원래이름
        String oriImgName = imgFile.getOriginalFilename();
        //실제 로컬에 저장된 상품 이미지 파일의 이름
        String imgName = "";
        //업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
        String imgUrl = "";

        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            /*
             사용자가 상품의 이미지를 등록했다면 저장할 경로와 파일의 이름, 파일,
             파일의 바이트 배열을 파일 업로드 파라미터로 전달하여 saveFile 메소드를 호출
             메소드 호출 결과 로컬에 저장된 파일 이름을 imgName 변수에 저장
             */
            imgName = fileService.saveFile(itemImgPath, oriImgName,
                                        imgFile.getBytes());
            //저장한 상품 이미지를 불러올 경로를 설정
            imgUrl = "/images/upload/" + imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        log.info("oriImgName : " + oriImgName );
        log.info("imgName : " + imgName );
        log.info("imgUrl : " + imgUrl );
        imgRepository.save(itemImg);

    }


}
