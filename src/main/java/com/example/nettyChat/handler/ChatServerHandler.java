package com.example.nettyChat.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

  private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    Channel incoming = ctx.channel();
    channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " has joined!\n");
    channels.add(incoming);
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    Channel incoming = ctx.channel();
    channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " has left!\n");
    channels.remove(incoming);
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    Channel incoming = ctx.channel();
    for (Channel channel : channels) {
      if (channel != incoming) {
        channel.writeAndFlush("[" + incoming.remoteAddress() + "] " + msg + "\n");
      }
    }
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    Channel incoming = ctx.channel();
    System.out.println("ChatClient:" + incoming.remoteAddress() + "online");
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Channel incoming = ctx.channel();
    System.out.println("ChatClient:" + incoming.remoteAddress() + "offline");
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    Channel incoming = ctx.channel();
    System.out.println("ChatClient:" + incoming.remoteAddress() + "异常");
    cause.printStackTrace();
    ctx.close();
  }
}
