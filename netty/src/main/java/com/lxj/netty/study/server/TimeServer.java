package com.lxj.netty.study.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
	public static void main(String[] args) throws Exception {
		TimeServer timeServer = new TimeServer();
		timeServer.bind(9001);
	}
	
	private void bind(int port) throws Exception{
		//两个线程组用户网络事件处理
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try{
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.childHandler(new ChannelInitializer<SocketChannel>(){
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new TimeServerHandler());					
				}
			});
			
			//绑定端口，同步等待
			ChannelFuture f = serverBootstrap.bind(port).sync();
			
			//登陆服务器监听端口关闭
			f.channel().closeFuture().sync();
			
			
		}finally{
			//优雅退出，释放资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	private class TimeServerHandler extends ChannelHandlerAdapter{
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			
		}
		
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			ctx.flush();
		}
		
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			ctx.flush();
		}
	}
	
}
