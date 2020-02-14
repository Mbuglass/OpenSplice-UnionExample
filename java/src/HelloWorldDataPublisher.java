/************************************************************************
 * LOGICAL_NAME:    HelloWorldPublisher.java
 * FUNCTION:        Publisher's main for the HelloWorld Union OpenSplice programming example.
 * MODULE:          OpenSplice Union example for the java programming language.
 *                  Based on the default HelloWorld example included with OpenSplice.
 * DATE             February 2020.
 ************************************************************************/

import DDS.DataWriter;
import DDS.HANDLE_NIL;
import HelloWorldData.Msg;
import HelloWorldData.MsgDataWriter;
import HelloWorldData.MsgDataWriterHelper;
import HelloWorldData.MsgTypeSupport;

public class HelloWorldDataPublisher {

	public static void main(String[] args) {
		DDSEntityManager mgr = new DDSEntityManager();
		String partitionName = "HelloWorld example";

		// create Domain Participant
		mgr.createParticipant(partitionName);

		// create Type
		MsgTypeSupport msgTS = new MsgTypeSupport();
		mgr.registerType(msgTS);

		// create Topic
		mgr.createTopic("HelloWorldData_Msg");

		// create Publisher
		mgr.createPublisher();

		// create DataWriter
		mgr.createWriter();

		// Publish Events
		DataWriter dwriter = mgr.getWriter();
		MsgDataWriter HelloWorldWriter = MsgDataWriterHelper.narrow(dwriter);
		Msg msgInstance = new Msg();

		// Used to define the Message_Type
		HelloWorldData.Message_Type discriminator;

		// Used to assign values to each type
		HelloWorldData.System_Info_Type sysInfoMsg = new HelloWorldData.System_Info_Type();
		HelloWorldData.System_Error_Type sysErrorMsg = new HelloWorldData.System_Error_Type();
		HelloWorldData.Bus_Message_Type sysBusMsg = new HelloWorldData.Bus_Message_Type();

		// Set Message_Type of message
		// First message will be of type SYSTEM_INFO_MESSAGE
		discriminator = (HelloWorldData.Message_Type)HelloWorldData.Message_Type.SYSTEM_INFO_MESSAGE;

		// Set message values
		sysInfoMsg.process = "process.exe";
		sysInfoMsg.thread = 6892;
		sysInfoMsg.desc = "Application started.";
		msgInstance.msgID = 1;
		msgInstance.msg.msg1(discriminator,sysInfoMsg);

		// Display message in console
		System.out.println("=== [Publisher] writing a message containing :");
		System.out.println("    Message ID   : " + msgInstance.msgID);
		System.out.println("    Message_Type : SYSTEM_INFO_MESSAGE");
		System.out.println("    Process      : " + sysInfoMsg.process);
		System.out.println("    Thread       : " + sysInfoMsg.thread);
		System.out.println("    Description  : " + sysInfoMsg.desc);

		// Publish message
		HelloWorldWriter.register_instance(msgInstance);
		int status = HelloWorldWriter.write(msgInstance, HANDLE_NIL.value);
		ErrorHandler.checkStatus(status, "MsgDataWriter.write");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Set Message_Type of message
		// Second message will be of type SYSTEM_ERROR_MESSAGE
		discriminator  = (HelloWorldData.Message_Type)HelloWorldData.Message_Type.SYSTEM_ERROR_MESSAGE;

		// Set message values
		sysErrorMsg.errorCode = 1321;
		msgInstance.msgID = 2;
		msgInstance.msg.msg2(discriminator,sysErrorMsg);

		// Display message in console
		System.out.println("=== [Publisher] writing a message containing :");
		System.out.println("    Message ID   : " + msgInstance.msgID);
		System.out.println("    Message_Type : SYSTEM_ERROR_MESSAGE");
		System.out.println("    Error Code   : " + sysErrorMsg.errorCode);

		// Publish message
		HelloWorldWriter.register_instance(msgInstance);
		status = HelloWorldWriter.write(msgInstance, HANDLE_NIL.value);
		ErrorHandler.checkStatus(status, "MsgDataWriter.write");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Set Message_Type of message
		// Third message will be of type BUS_MESSAGE
		discriminator  = (HelloWorldData.Message_Type)HelloWorldData.Message_Type.BUS_MESSAGE;

		// Set message values
		sysBusMsg.busId = 13;
		sysBusMsg.data = 10101110;
		sysBusMsg.length = 8;
		msgInstance.msgID = 3;
		msgInstance.msg.msg3(discriminator,sysBusMsg);

		// Display message in console
		System.out.println("=== [Publisher] writing a message containing :");
		System.out.println("    Message ID   : " + msgInstance.msgID);
		System.out.println("    Message_Type : BUS_MESSAGE");
		System.out.println("    Bus ID       : " + sysBusMsg.busId);
		System.out.println("    Data         : " + sysBusMsg.data);
		System.out.println("    Length       : " + sysBusMsg.length);

		// Publish message
		HelloWorldWriter.register_instance(msgInstance);
		status = HelloWorldWriter.write(msgInstance, HANDLE_NIL.value);
		ErrorHandler.checkStatus(status, "MsgDataWriter.write");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// clean up
		mgr.getPublisher().delete_datawriter(HelloWorldWriter);
		mgr.deletePublisher();
		mgr.deleteTopic();
		mgr.deleteParticipant();

	}
}