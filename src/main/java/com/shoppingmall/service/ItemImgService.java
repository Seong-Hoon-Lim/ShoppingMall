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

import javax.persistence.EntityNotFoundException;

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
            imgUrl = "/images/item/" + imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        log.info("oriImgName : " + oriImgName );
        log.info("imgName : " + imgName );
        log.info("imgUrl : " + imgUrl );
        imgRepository.save(itemImg);

    }

    public void updateItemImg(Long itemImgId, MultipartFile imgFile) throws Exception {

        //상품 이미지를 수정한 경우 상품 이미지를 업데이트
        if (!imgFile.isEmpty()) {
            //상품 이미지 아이디를 이용하여 기존에 저장했던 상품 이미지 엔티티를 조회
            ItemImg savedItemImg = imgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //기존에 등록된 상품 이미지 파일이 있을 경우 해당 파일을 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgPath + "/" +
                                        savedItemImg.getImgName());
            }

            /*
             변경된 상품 이미지 정보를 세팅하고 저장
             상품등록 때와 다르게 save() 메소드를 호출 하지 않음
             savedItemImg 엔티티가 현재 영속상태이므로 데이터를 변경하는 것만으로
             변경 감지 기능이 동작하여 자동으로 트랜잭션이 끝날 때 update 쿼리가 실행됨
             */
            String oriImgName = imgFile.getOriginalFilename();
            String imgName = fileService.saveFile(itemImgPath,
                    oriImgName, imgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);

        }

    }


}
