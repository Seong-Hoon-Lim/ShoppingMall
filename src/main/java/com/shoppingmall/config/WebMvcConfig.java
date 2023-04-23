package com.shoppingmall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 업로드한 파일을 읽어올 경로를 설정하는 WebMvcConfigurer 인터페이스를
 * 구현하는 클래스. addResourceHandlers 메소드를 통해서 로컬 컴퓨터에
 * 업로드 된 파일을 찾을 위치를 설정
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //프로퍼티에 설정한 uploadPath 의 값을 읽어옴.
    @Value("${uploadPath}")
    String uploadPath;

    /*
     웹브라우저에 입력하는 url 에 /images 로 시작하는 경우 uploadPath에 설정한
     폴더를 기준으로 파일을 읽어오도록 설정
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                //로컬에 저장된 파일을 읽어올 root 경로를 설정
                .addResourceLocations(uploadPath);
    }
}
