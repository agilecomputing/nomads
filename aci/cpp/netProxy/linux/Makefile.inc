# Guess the architecture
ARCH = $(shell sh ./guessArch.sh)

CPP = g++

NOMADS_HOME = ../../../..
ACI_HOME = $(NOMADS_HOME)/aci
EXTERNALS = $(NOMADS_HOME)/externals
EXTERNALS_LIB = $(EXTERNALS)/lib/$(ARCH)
MOCKETS_HOME = $(NOMADS_HOME)/mockets
UTIL_HOME = $(NOMADS_HOME)/util
MSGPACK_HOME = $(EXTERNALS)/msgpack
LIBZ_HOME = $(EXTERNALS)/zlib
LZMA_HOME = $(EXTERNALS)/xz
LZMA_LIB = $(LZMA_HOME)/src/liblzma/api

LIB_FOLDER = linux
LD_FLAGS = -ldl -lpcap -lpthread

CPPFLAGS = -g -DUNIX -DLINUX -DENABLE_DEBUG -DERROR_CHECKING -DLITTLE_ENDIAN_SYSTEM \
			-I.. -I../.. \
			-I$(EXTERNALS)/include/pcap \
			-I$(EXTERNALS)/include \
			-I$(UTIL_HOME)/cpp \
			-I$(MOCKETS_HOME)/cpp

LIB_LIST = $(MOCKETS_HOME)/cpp/$(LIB_FOLDER)/libmockets.a \
			$(UTIL_HOME)/cpp/$(LIB_FOLDER)/libsecurity.a \
			$(UTIL_HOME)/cpp/$(LIB_FOLDER)/libutil.a \
			$(EXTERNALS_LIB)/libcrypto.a \
			$(MSGPACK_HOME)/$(LIB_FOLDER)/libmsgpack.a \
			$(LIBZ_HOME)/$(LIB_FOLDER)/libz.a \
			$(LZMA_HOME)/$(LIB_FOLDER)/liblzma.a

sources = $(wildcard ../*.cpp)
objects = $(sources:../%.cpp=%.o)
