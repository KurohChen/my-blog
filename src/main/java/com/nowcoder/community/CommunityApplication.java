package com.nowcoder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CommunityApplication {
	//在构造器调用完一后被执行
	//该代码用于解决redis和elasticsearch都调用Netty启动引起的冲突
	@PostConstruct
	public void init(){
		//在setAvailableProcessors(int availableProcessors)
		System.setProperty("es.set.netty.runtime.available.processors","false");
	}

	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
