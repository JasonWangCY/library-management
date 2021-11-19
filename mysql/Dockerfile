FROM mysql:5.5.58

RUN apt update -y && apt upgrade -y
RUN apt install -y wget net-tools nano curl

ENV HOME=/usr
WORKDIR ${HOME}
RUN mkdir -p ./java
COPY ./jre-6u26-linux-x64.bin ./java/

WORKDIR ${HOME}/java
RUN chmod a+x jre-6u26-linux-x64.bin
RUN ./jre-6u26-linux-x64.bin

ENV JAVA_HOME=/usr/java/jre1.6.0_26
ENV PATH=$JAVA_HOME/bin:$PATH
ENV CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

ENV HOME=/home
RUN mkdir -p ${HOME}/Project/mysql
WORKDIR ${HOME}/Project/mysql
COPY mysql-connector-java-5.1.47.jar ./
ENV CLASSPATH=${HOME}/Project/mysql/mysql-connector-java-5.1.47.jar:${CLASSPATH}}

WORKDIR ${HOME}/java
COPY ./jdk-6u38-linux-x64.bin ./
RUN chmod a+x jdk-6u38-linux-x64.bin
RUN ./jdk-6u38-linux-x64.bin
RUN mkdir /usr/lib/jvm 
RUN mv jdk1.6.0_38 /usr/lib/jvm/jdk1.6.0_38
RUN update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/jdk1.6.0_38/bin/javac 1
RUN update-alternatives --config javac
WORKDIR ${HOME}/Project







