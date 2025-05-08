run: 
	mvn  javafx:run

install:
	mvn clean install

server:
	clear && cd "/media/cabylock/file/source_code/bomberman/src/main/java/core/system/network/" && javac GameServer.java && java GameServer

client:
	clear && cd "/media/cabylock/file/source_code/bomberman/src/main/java/core/system/network/" && javac GameClient.java && java GameClient

check-port:
	sudo ss -tuln | grep 8080

stop-server:
	sudo kill -9 $(sudo lsof -t -i:8080)

copy:
	cp -r /media/cabylock/file/source_code/server/bomberman/ /media/cabylock/file/source_code/client/


# // change icon, add sound system, modify sprite. 
