FROM hub.c.163.com/library/java:8-jre
ADD COM.JZB.PROJECT/COM.JZB.EURAKE.MODULE/target/*.jar  
ADD COM.JZB.PROJECT/COM.JZB.CONFIG.MODULE/target/*.jar 
EXPOSE  7101
EXPOSE  7506 
COPY start_cloud.sh /usr/bin/start_eurake-config.sh
RUN chmod +x /usr/bin/start_eurake-config.sh
CMD nohup sh -c "start_eurake-config.sh"