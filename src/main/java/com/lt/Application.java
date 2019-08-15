package com.lt;

import com.lt.nettyServer.NettyServer;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.lt.dal.mapper")
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
        NettyServer server = new NettyServer();
        server.startServer();
    }

}
