package com.netty.study.decoder.messagepack;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {

	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		final byte[] array;
		final int length = msg.readableBytes();//获取需要解码的字节流长度
		array = new byte[length];
		msg.getBytes(msg.readerIndex(),array,0,length);//获取需要解码的字节流数组
		MessagePack msgPack = new MessagePack();
		out.add(msgPack.read(array));//通过messagePack反序列化到对象中
		
	}

}
