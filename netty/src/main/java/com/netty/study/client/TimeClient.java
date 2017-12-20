package com.netty.study.client;

import java.util.logging.Logger;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeClient {
	private static final Logger logger = Logger.getLogger(TimeClient.class.getName());
	
	public static void main(String[] args) throws Exception {
		TimeClient timeClient = new TimeClient();
		timeClient.connect(9001, "localhost");
	}
	
	public void connect(int port,String host) throws Exception{
		//定义io读写线程组
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
					ch.pipeline().addLast(new StringDecoder());
					ch.pipeline().addLast(new TimeClientHandler());
				}
				
			});
			
			ChannelFuture f = b.connect(host,port).sync();
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
	
	public class TimeClientHandler extends ChannelHandlerAdapter{
		private int counter;
		private byte[] req;
		public TimeClientHandler(){
			req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
		}
		
		public void channelActive(ChannelHandlerContext ctx){
			ByteBuf message = null;
			
			for(int i=0;i<100;i++){
				message = Unpooled.buffer(req.length);
				message.writeBytes(req);
				ctx.writeAndFlush(message);
			}
		}
		
		public void ChannelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
			ByteBuf buf = (ByteBuf)msg;
			byte[] req = new byte[buf.readableBytes()];
			buf.readBytes(req);
			String body = new String(req,"UTF-8");
			System.out.println("Now is :"+body+";the counter is :"+ ++counter);
		}
		
		public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
			logger.warning("Unexpected exception from downstream:"+cause.getMessage());
			ctx.close();
		}
	}
}
