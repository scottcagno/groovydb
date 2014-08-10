package com.cagnosolutions.db

import reactor.core.Environment
import reactor.event.dispatch.SynchronousDispatcher
import reactor.io.encoding.protobuf.ProtobufCodec
import reactor.net.netty.tcp.NettyTcpClient
import reactor.net.tcp.TcpClient
import reactor.net.tcp.spec.TcpClientSpec

import java.util.concurrent.TimeUnit

/**
 * Created by Scott Cagno.
 * Copyright Cagno Solutions. All rights reserved.
 */

class Client {

    // awesome

    TcpClient client
    def codec = new ProtobufCodec()

    Client(String host, int port) {
        client = new TcpClientSpec<String, String>(NettyTcpClient).
                env(new Environment()).
                dispatcher(new SynchronousDispatcher()).
                codec(codec).
                connect(host, port).
                get()

        def connection = client.open().await(5, TimeUnit.SECONDS)
        ["one", "two", "three", "four", "five"].each { String item ->
            connection.sendAndForget item
        }
        client.close().await(5, TimeUnit.SECONDS)

    }
}