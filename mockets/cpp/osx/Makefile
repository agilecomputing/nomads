CC  = gcc

CXXFLAGS = -g -DUNIX -DOSX -DENABLE_DEBUG -DERROR_CHECKING \
           -DLITTLE_ENDIAN_SYSTEM -I.. -I../../../util/cpp \
           -I/System/Library/Frameworks/JavaVM.framework/Headers

sources = $(wildcard ../*.cpp)
objects = $(sources:../%.cpp=%.o)

all: libmockets.a 

libmockets.a : $(objects) libutil.a libsecurity.a  
	ar -rcv libmockets.a $(objects)

libutil.a : 
	(cd ../../../util/cpp/osx; make)

libsecurity.a :  $(securityobjects)
	(cd ../../../util/cpp/osx; make) 

clean :
	rm -rf *.o *.a 

%.o : ../%.cpp
	$(CXX) -c $(CXXFLAGS) $<

