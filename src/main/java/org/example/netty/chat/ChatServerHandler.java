package org.example.netty.chat;

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
        for(Channel channel: channels) {
            channel.writeAndFlush(String.format("[SERVER] - %s has joined!%n", incoming.remoteAddress()));
        }
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for(Channel channel: channels) {
            channel.writeAndFlush(String.format("[SERVER] - %s has joined!%n", incoming.remoteAddress()));
        }
        channels.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String message) throws Exception {
        Channel incoming = channelHandlerContext.channel();
        for(Channel channel: channels) {
            if(channel != incoming) {
                channel.writeAndFlush(String.format("[%s]%s%n", incoming.remoteAddress(), message));
            }
        }
    }
}
