package com.example.nettyChat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class SocketServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(SocketServiceApplication.class, args);
  }
}
//
//class NettyServer {
//  public void start() {
//    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//    EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//    try {
//      ServerBootstrap b = new ServerBootstrap();
//      b.group(bossGroup, workerGroup)
//          .channel(NioServerSocketChannel.class)
//          .childHandler(new ChannelInitializer<SocketChannel>() {
//            @Override
//            protected void initChannel(SocketChannel ch) {
//              ChannelPipeline p = ch.pipeline();
//              p.addLast(new StringDecoder());
//              p.addLast(new StringEncoder());
//              p.addLast(new ServerHandler());
//            }
//          });
//
//      ChannelFuture f = b.bind(8888).sync();
//      System.out.println("Netty server started on port 8888");
//      f.channel().closeFuture().sync();
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    } finally {
//      workerGroup.shutdownGracefully();
//      bossGroup.shutdownGracefully();
//    }
//  }
//}
//
//class ServerHandler extends SimpleChannelInboundHandler<String> {
//  @Override
//  protected void channelRead0(ChannelHandlerContext ctx, String msg) {
//    System.out.println("Received message: " + msg);
//    ctx.writeAndFlush("Server received: " + msg + "\n");
//  }
//}

