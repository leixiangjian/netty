package com.netty.study.decoder.messagepack;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MsgpackEncoder<I> extends MessageToByteEncoder<I> {

	@Override
	protected void encode(ChannelHandlerContext ctx, I msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		MessagePack pack = new MessagePack();
		byte[] raw = pack.write(msg);//将pojo对象编码为byte数组
		out.writeBytes(raw);//写入到Bytebuf
	}

}
