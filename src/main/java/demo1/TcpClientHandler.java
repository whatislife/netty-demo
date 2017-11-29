package demo1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
//import util.LogCore;

public class TcpClientHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//		LogCore.BASE.info("client接收到服务器返回的消息:" + msg);
		System.out.println("client接收到服务器返回的消息:" + msg);
	}
}
