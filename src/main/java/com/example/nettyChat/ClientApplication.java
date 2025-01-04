package com.example.nettyChat;

import com.example.nettyChat.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class ClientApplication {
  private final String host;
  private final int port;

  public static void main(String[] args) throws Exception {
    new ClientApplication("localhost", 8080).run();
  }

  public void run() throws  Exception {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap bootstrap = new Bootstrap()
          .group(group)
          .channel(NioSocketChannel.class)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
              ChannelPipeline pipeline = socketChannel.pipeline();
              pipeline.addLast(new StringDecoder());
              pipeline.addLast(new StringEncoder());
              pipeline.addLast(new ClientHandler());
            }
          });

      Channel channel = bootstrap.connect(host, port).sync().channel();
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

      while (true) {
        String line = in.readLine();
        if (line == null) {
          break;
        }
        channel.writeAndFlush(line + "\r\n");
      }
    } finally {
      group.shutdownGracefully();
    }
  }

}
