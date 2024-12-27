package com.fastcampus.boardadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan // 루트 경로 기준으로 모든 설정 클래스로부터 configuraion property가 있는지를 스캔
@SpringBootApplication
public class BoardAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardAdminApplication.class, args);
	}

}
