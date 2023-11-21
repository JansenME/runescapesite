package com.maulsinc.runescape;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.lang.management.ManagementFactory;

@SpringBootApplication
public class RunescapesiteApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		MDC.put("process_id", String.valueOf(ManagementFactory.getRuntimeMXBean().getPid()));

		SpringApplication.run(RunescapesiteApplication.class, args);
	}

}
