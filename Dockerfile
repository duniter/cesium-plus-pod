# Image for Cesium releases on Linux.
#
#
# Building this image:
#   docker build . --network=host -t duniter/cesium-plus-pod
#
# Test:
#   docker run --net=host -t duniter/cesium-plus-pod
# Test (interactive mode + bash mode):
#   docker run -i --net=host -t duniter/cesium-plus-pod bash
#
# Pull base image.
FROM airdock/oracle-jdk:1.8

ARG VERSION=1.1.3
ARG LIBSODIUM_VERSION=1.0.14

# Installing dependencies
RUN apt-get update && \
  apt-get --force-yes --yes install wget unzip build-essential

# Installing libsodium
RUN wget https://download.libsodium.org/libsodium/releases/libsodium-${LIBSODIUM_VERSION}.tar.gz && \
    tar -xzf libsodium-*.tar.gz && rm *.tar.gz && mv libsodium-* libsodium && \
    cd libsodium  && \
    ./configure && \
    make && make check && \
    make install

# Create compiling user
RUN mkdir /cesium-plus-pod && \
	adduser --system --group --quiet --shell /bin/bash --home /cesium-plus-pod cesium-plus-pod && \
	chown cesium-plus-pod:cesium-plus-pod /cesium-plus-pod
WORKDIR /cesium-plus-pod

RUN cd /cesium-plus-pod && \
    wget https://github.com/duniter/cesium-plus-pod/releases/download/cesium-plus-pod-${VERSION}/cesium-plus-pod-${VERSION}-standalone.zip && \
    unzip *.zip && rm *.zip && mv cesium-plus-pod-* cesium-plus-pod && \
    mkdir cesium-plus-pod/data && \
    chown -R cesium-plus-pod:cesium-plus-pod cesium-plus-pod

RUN ln -s /cesium-plus-pod/cesium-plus-pod/bin/elasticsearch /usr/bin/cesium-plus-pod

VOLUME /cesium-plus-pod/cesium-plus-pod
EXPOSE 9200 9400

USER cesium-plus-pod
WORKDIR /cesium-plus-pod

ENTRYPOINT ["/usr/bin/cesium-plus-pod"]
CMD []
