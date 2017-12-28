package com.netty.study.codes.bytebuf;

import java.nio.ByteBuffer;

public class ByteBufExample1 {

	public static void main(String[] args) {
		ByteBuffer bytebuf = ByteBuffer.allocate(88);
		String value = "netty bytebuf 测试";
		bytebuf.put(value.getBytes());
		bytebuf.flip();
		
		int remain = bytebuf.remaining();
		byte[] ararys = new byte[remain];
		bytebuf.get(ararys);
		System.out.println(remain+"=="+new String(ararys));
	}

}
