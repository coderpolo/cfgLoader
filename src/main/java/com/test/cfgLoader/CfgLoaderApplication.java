package com.test.cfgLoader;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class CfgLoaderApplication {

	public static void main(String[] args)
	{
//		SpringApplication.run(CfgLoaderApplication.class, args);
		SpringApplication app = new SpringApplication(CfgLoaderApplication.class);
		app.setBannerMode(Banner.Mode.CONSOLE);
		app.addListeners();
		app.run(args);
	}
}
