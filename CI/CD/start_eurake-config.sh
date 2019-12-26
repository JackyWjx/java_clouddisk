#!/bin/sh
nohup java -Xss256k -Xms64m  -Xmx512m -jar jzb.eureka-1.0.jar >> /opt/eureka.log &
nohup java -Xss256k -Xms64m  -Xmx512m -jar jzb.config-1.0.jar >> /opt/config.log &