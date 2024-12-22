package com.example.nettyChat.config;

import com.example.nettyChat.handler.ChatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Configuration
public class SocketServiceConfig {
  @Bean
  public Mono<Void> startNettyServer() {
    return Mono.fromRunnable(() -> {
      EventLoopGroup bossGroup = new NioEventLoopGroup(1);
      EventLoopGroup workerGroup = new NioEventLoopGroup();

      try {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer() {
              @Override
              protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new ChatServerHandler());
              }
            });

        ChannelFuture f = b.bind(8888).sync();
        System.out.println("Chat server started on port 8888");
        f.channel().closeFuture().sync();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } finally {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
      }
    }).subscribeOn(Schedulers.boundedElastic()).then();
  }

  @Bean
  public CommandLineRunner runner(Mono<Void> startNettyServer) {
    return args -> startNettyServer.subscribe();
  }
}

