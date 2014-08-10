package com.cagnosolutions.main

import groovy.transform.CompileStatic
/**
 * Created by Scott Cagno.
 * Copyright Cagno Solutions. All rights reserved.
 */

@CompileStatic
class Main {

    /*
    static void main(String... args) {

        def rowco = new Comparator() {
            int compare(Object o1, Object o2) {
                o1 > o2 ? 1 : 0
            }
        }
        def colco = new Comparator() {
            int compare(Object o1, Object o2) {
                o1 > o2 ? 1 : 0
            }
        }
        def table = new TreeBasedTable<String,String,String>(rowco, colco)

    }
    */


    /*
    static void main(String... args) {
        def server = new Server(12345)
        def client = new Client("localhost", 12345)
    }
    */


    /*
    static void main(String... args) {

        int requests = 10000000 // 10 million
        long startTime, endTime
        double elapsed, throughput
        def latch // mutex

        // pretty print numbers closure
        def prettyNums = { number ->
            number.toString().replaceAll "(\\d)(?=(\\d{3})+\$)", "\$1,"
        }

        // start timer closure
        def startTimer = {
            println "Starting micro benchmark throughput test for 30 seconds using ${prettyNums requests} requests..."
            latch = new CountDownLatch(requests)
            startTime = System.currentTimeMillis()
        }

        // end timer closure
        def endTimer = {
            latch.await(30, TimeUnit.SECONDS);
            endTime = System.currentTimeMillis()
            elapsed = (endTime - startTime) * 1.0
            throughput = requests / (elapsed / 1000)
            println "~${prettyNums throughput as int} req/sec, ~${prettyNums(throughput * 60 as long)} req/min"
        }

        def reactor = Reactors.reactor(new Environment(), new SynchronousDispatcher())

        // handler
        def handleEventData = { eventData ->
            if(eventData == null)
                throw new IOException("Event data is null!")
        }

        // consumer
        reactor.on(Selectors.R("hello")) { Event<String> ev ->
            if(ev.headers['specialHeader']) { // Events can have metadata
                handleEventData(ev.data)
            }
        }

        // start throughput timer
        startTimer()

        // publish one event per request
        for (int i in 0..requests) {

            // notify the reactor that the next randomly
            // generated request is ready to be handled
            reactor.notify for: 'hello', data: "data: ${String.valueOf(i)}", specialHeader: "value: ${String.valueOf(i)}"
        }

        // Stop throughput timer and output metrics
        endTimer()

    }
    */


}
