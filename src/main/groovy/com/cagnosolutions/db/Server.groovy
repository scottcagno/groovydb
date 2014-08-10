package com.cagnosolutions.db

import reactor.core.Environment
import reactor.event.dispatch.SynchronousDispatcher
import reactor.function.Consumer
import reactor.io.encoding.protobuf.ProtobufCodec
import reactor.net.NetChannel
import reactor.net.netty.tcp.NettyTcpServer
import reactor.net.tcp.TcpServer
import reactor.net.tcp.spec.TcpServerSpec

/**
 * Created by Scott Cagno.
 * Copyright Cagno Solutions. All rights reserved.
 */

class Server {

    TcpServer server
    def codec = new ProtobufCodec()

    Server(int port) {
        server = new TcpServerSpec<String, String>(NettyTcpServer).
            env(new Environment()).
            dispatcher(new SynchronousDispatcher()).
            listen(port).
            codec(codec).
            consume({ conn ->
                conn.consume({ data ->
                    consumerMock.any(data)
                } as Consumer<String>)
            } as Consumer<NetChannel<String, String>>).
            get()


    }

    def consumerMock = Mock(Consumer) {
        data.size() * accept(_)
    }

}
