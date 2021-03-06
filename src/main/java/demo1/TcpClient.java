package demo1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class TcpClient {
	public static String HOST = "127.0.0.1";
	public static int PORT = 9999;

	public static Bootstrap bootstrap = getBootstrap();
	public static Channel channel = getChannel(HOST, PORT);

	/**
	 * 初始化Bootstrap   
	 */
	public static final Bootstrap getBootstrap() {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class);
		b.handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
				pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
				pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
				pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
				pipeline.addLast("handler", new TcpClientHandler());
			}
		});
		b.option(ChannelOption.SO_KEEPALIVE, true);
		return b;
	}

	public static final Channel getChannel(String host, int port) {
		Channel channel = null;
		try {
			channel = bootstrap.connect(host, port).sync().channel();
		} catch (Exception e) {
//			LogCore.BASE.error("连接Server(IP{},PORT{})失败", host, port, e);
			System.out.println("连接Server(IP{},PORT{})失败"+host+port+e);
			return null;
		}
		return channel;   
	}

	public static void sendMsg(String msg) throws Exception {
		if (channel != null) {
			channel.writeAndFlush(msg).sync();
		} else {
//			LogCore.BASE.warn("消息发送失败,连接尚未建立!");
			System.out.println("消息发送失败,连接尚未建立!");
			
			//whatis-life
			
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			long t0 = System.nanoTime();
			for (int i = 0; i < 100; i++) {
				TcpClient.sendMsg(i + "你好1");
			}
			long t1 = System.nanoTime();
//			LogCore.BASE.info("time used:{}", t1 - t0);
			System.out.println("time used:{}"+(t1 - t0));
		} catch (Exception e) {
//			LogCore.BASE.error("main err:", e);
			System.err.println("main err:"+e);
		}
	}
}
