/************************************************************************
 * LOGICAL_NAME:    HelloWorldSubscriber.java
 * FUNCTION:        Subscriber's main for the HelloWorld Union OpenSplice programming example.
 * MODULE:          OpenSplice Union example for the java programming language.
 *                  Based on the default HelloWorld example included with OpenSplice.
 * DATE             February 2020.
 ************************************************************************/

import DDS.ANY_INSTANCE_STATE;
import DDS.ANY_SAMPLE_STATE;
import DDS.ANY_VIEW_STATE;
import DDS.DataReader;
import DDS.LENGTH_UNLIMITED;
import DDS.SampleInfoSeqHolder;
import HelloWorldData.MsgDataReader;
import HelloWorldData.MsgDataReaderHelper;
import HelloWorldData.MsgSeqHolder;
import HelloWorldData.MsgTypeSupport;

public class HelloWorldDataSubscriber {

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

        // create Subscriber
        mgr.createSubscriber();

        // create DataReader
        mgr.createReader();

        // Read Events

        DataReader dreader = mgr.getReader();
        MsgDataReader HelloWorldReader = MsgDataReaderHelper.narrow(dreader);

        MsgSeqHolder msgSeq = new MsgSeqHolder();
        SampleInfoSeqHolder infoSeq = new SampleInfoSeqHolder();

        System.out.println("=== [Subscriber] Ready ...");
        boolean terminate = false;
        int msgCount = 0;
        int count = 0;
        while (!terminate && count < 1500) { // We dont want the example to run indefinitely
            HelloWorldReader.take(msgSeq, infoSeq, LENGTH_UNLIMITED.value,
                    ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value,
                    ANY_INSTANCE_STATE.value);
            for (int i = 0; i < msgSeq.value.length; i++) {
                msgCount++;
                // Get common values from message
                System.out.println("=== [Subscriber] message received :");
                System.out.println("    message ID  : " + msgSeq.value[i].msgID);
                // If statements to identify message type and extract appropriate data
                // A switch would have been more elegant but doesn't support the datatypes
                HelloWorldData.Message_Type d = msgSeq.value[i].msg.discriminator();
                if (d.equals((HelloWorldData.Message_Type)HelloWorldData.Message_Type.SYSTEM_INFO_MESSAGE)) {
                    System.out.println("    Message_Type: SYSTEM_INFO_MESSAGE");
                    System.out.println("    Message     : " + msgSeq.value[i].msg.msg1().process);
                    System.out.println("    Thread      : " + msgSeq.value[i].msg.msg1().thread);
                    System.out.println("    Description : " + msgSeq.value[i].msg.msg1().desc);
                } else if (d.equals((HelloWorldData.Message_Type)HelloWorldData.Message_Type.SYSTEM_ERROR_MESSAGE)) {
                    System.out.println("    Message_Type: SYSTEM_ERROR_MESSAGE");
                    System.out.println("    Error Code  : " + msgSeq.value[i].msg.msg2().errorCode);
                } else if (d.equals((HelloWorldData.Message_Type)HelloWorldData.Message_Type.BUS_MESSAGE)) {
                    System.out.println("    Message_Type: BUS_MESSAGE");
                    System.out.println("    BUS ID      : " + msgSeq.value[i].msg.msg3().busId);
                    System.out.println("    Data        : " + msgSeq.value[i].msg.msg3().data);
                    System.out.println("    Length      : " + msgSeq.value[i].msg.msg3().length);
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ie) {
                // nothing to do
            }
            terminate = (msgCount >= 3);
            ++count;
        }
        HelloWorldReader.return_loan(msgSeq, infoSeq);

        // clean up
        mgr.getSubscriber().delete_datareader(HelloWorldReader);
        mgr.deleteSubscriber();
        mgr.deleteTopic();
        mgr.deleteParticipant();

    }
}
