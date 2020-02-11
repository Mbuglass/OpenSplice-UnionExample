# OpenSplice Union Example

This repository contains a modified version of the OpenSplice HelloWorld example, adapted to make use of an IDL Union to demonstrate how to assign values to a union as well as send/recieve this data.
Currently, this repository contains only an example for C++ but may be updated to contain more languages in future. This is not an official example, and is meant only to provide some guidance for those interested in getting started with unions in OpenSplice.

## Description

Three types of message are defined in the file HelloWorldData.idl:

 - SYSTEM_INFO_MESSAGE
 - SYSTEM_ERROR_MESSAGE
 - BUS_MESSAGE

Each of the message types contains some simple variables, generally either a string or long.
For more information on IDL refer to the OpenSplice IDL PreProcessor guide.

The publisher included in this example will publish three messages, one of each type.
Upon reading these messaged, the subscriber will use a switch, where each case is a message type, to ensure that only the relevant data is read for the message type.

After sending the third message the publisher will shut down. The subscriber will either shut down after reading the third message or if it times-out.


## How to run

To run this example, replace the existing publisher and subscriber files in examples\dcps\HelloWorld\cpp\src with the ones provided, and replace the file examples\dcps\HelloWorld\idl\HelloWorldData.idl with the provided HelloWorldData.idl. 
Process the IDL file using ` idlpp -N -S -l cpp HelloWorldData.idl
`. Copy these files and paste them into HelloWorld\cpp\standalone overwriting any files that may already be there.
Recompile the publisher and subscriber, then attempt to run the example as normal. More information on the default examples and how to build them can be found in /examples/dcps/README.html

### Compiling

#### Windows

To build all examples (if you haven't done so before), you can use the All_C_and_CPlusPlus.sln in the \examples\ directory. However, if you wish to rebuild just the HelloWorld example, use the vcxproj files in the \examples\dcps\HelloWorld\cpp\standalone directory.
*Note: If you use All_C_and_CPlusPlus.sln you may get errors for the HelloWorld example in other languages* 

#### Linux

If attempting to compile this example on Linux, it may be best to do so using the ospllauncher. An issue with the Makefiles could result in build errors when attempting to compile this example. From the examples section in the launcher, select the HelloWorld example with cpp as the language. Click the build button (Looks like a down arrow). Once this is done, click the run button and the example should run.
