package com.example.nettyChat.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

public class ServerHandler extends ChannelInboundHandlerAdapter {
  private final List<Channel> channels;

  public ServerHandler(List<Channel> channels) {
    this.channels = channels;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    String message = (String) msg;
    System.out.println("Received message from client: " + message);

    // broadcast the mesage to all connected clients except the sender
    ctx.channel().parent().writeAndFlush("[" + ctx.channel().remoteAddress()
    + "]: " + message + "\n");
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
