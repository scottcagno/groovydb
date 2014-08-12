package com.cagnosolutions.db

import groovy.transform.CompileStatic
import reactor.core.Environment
import reactor.event.dispatch.SynchronousDispatcher
import reactor.function.Consumer
import reactor.io.encoding.protobuf.ProtobufCodec
import reactor.net.NetChannel
import reactor.net.netty.tcp.NettyTcpClient
import reactor.net.netty.tcp.NettyTcpServer
import reactor.net.tcp.spec.TcpClientSpec
import reactor.net.tcp.spec.TcpServerSpec

import java.util.concurrent.TimeUnit

/**
 * Created by Scott Cagno.
 * Copyright Cagno Solutions. All rights reserved.
 */

@CompileStatic
class DB {

    Environment sEnv
    Environment cEnv

    def host = "localhost"
    def port = 12345
    def codec = new ProtobufCodec()
    def consumerMock = Mock(Consumer) { data.size() * accept(_) }

    def server = new TcpServerSpec<Pojo, Pojo>(NettyTcpServer).
        env(sEnv).
        dispatcher(new SynchronousDispatcher()).
        listen(port).
        codec(codec).
        consume({ conn ->
            conn.consume({ pojo ->
                consumerMock.accept(pojo)
            } as Consumer<Pojo>)
        } as Consumer<NetChannel<Pojo, Pojo>>).
        get()


    def client = new TcpClientSpec<Pojo, Pojo>(NettyTcpClient).
        env(cEnv).
        dispatcher(new SynchronousDispatcher()).
        codec(codec).
        connect(host, port).
        get()


    def startServer() {
        server.start().await(5, TimeUnit.SECONDS)
        server.start().addShutdownHook {

        }

        // ? server.start().addShutdownHook {}
        // ? server.shutdown().await(5, TimeUnit.SECONDS)
    }

    def runClient() {
        def connection = client.open().await(5, TimeUnit.SECONDS)
        [new Pojo("one"),new Pojo("one"),new Pojo("three")].each { Pojo item ->
            connection.sendAndForget item
        }
        client.close().await(5, TimeUnit.SECONDS)
    }

    static class Pojo {
        public Pojo() {}
        public Pojo(String name) { this.name = name }
        String name
    }
}
