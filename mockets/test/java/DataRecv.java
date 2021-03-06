//package us.ihmc.mockets.tests.messagexmit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import us.ihmc.mockets.MessageMocket;
import us.ihmc.mockets.MessageServerMocket;
import us.ihmc.mockets.Mocket;
import us.ihmc.mockets.Sender;

import us.ihmc.util.ByteConverter;

public class DataRecv
{
    public static void main (String[] args) throws Exception
    {
        boolean useSockets = false;
        final byte[] ack = "ACK".getBytes (CHARSET);
        
        int i = 0;
        while (i < args.length) {
            if (args[i].equals("--sockets")) {
                useSockets = true;
            } 
            else {
                // finished option parsing
                break;
            }
            ++i;
        }

        int argsLeftToParse = args.length - i;
        if (argsLeftToParse != 0) {
            System.out.println ("usage: java DataRecv [--sockets]");
            System.exit (1);
        }

        /* initialize logging */
        Logger logger = Logger.getLogger ("us.ihmc.mockets");
        //FileHandler fh = new FileHandler ("DataRecv%g.log");
        //fh.setLevel (Level.INFO);
        //fh.setFormatter (new SimpleFormatter());
        //logger.addHandler (fh);
        logger.setLevel (Level.OFF);

        /* create server socket and wait for connection */
        if (!useSockets) {
            System.out.println ("DataRecv: creating MessageServerMocket");
            MessageServerMocket servMocket = new MessageServerMocket (LOCAL_PORT);
            byte[] buffer = new byte[MessageMocket.getMaximumMTU()];
            
            /* wait for connection */
            MessageMocket mocket = servMocket.accept();
            Sender rlsq = mocket.getSender (true, true);
            System.out.println ("DataRecv: MessageServerMocket accepted a connection");
            
            /* read size of data to receive */
            int rr = mocket.receive (buffer, 0, buffer.length);
            if (rr != 8) {
                System.err.println ("Wrong data size message");
                System.exit (10);
            }
            long datasize = ByteConverter.from8BytesToLong (buffer, 0);
            System.out.println ("DataRecv: will read [" + datasize + "] bytes.");

            /* send ack */
            System.arraycopy (ack, 0, buffer, 0, ack.length);
            rlsq.send (buffer, 0, ack.length);
            
            /* start receiving data */
            System.out.println ("DataRecv: start receiving Data");
            long bytesRead = 0;
            long startTime = System.currentTimeMillis();

            while (true) {
                long bytesToRead = Math.min ((long)buffer.length, datasize - bytesRead);
                if (bytesToRead == 0) {
                    break;
                }
                if ((i = mocket.receive (buffer, 0, (int)bytesToRead)) >= 0) {
                    bytesRead += i;
                    float speed = (float)(bytesRead * 1000)/ (float)(System.currentTimeMillis() - startTime);
                    System.out.println ("DataRecv: received " + i + " bytes (" + bytesRead + " total)" +
                                        " - average bandwidth: " + speed + " bytes/sec");
                } 
                else {
                    System.out.println ("DataRecv: EOF reached so closing connection");
                    break;
                }
            }
        
            Mocket.Statistics stats = mocket.getStatistics();
            System.out.println ("Mocket statistics:");
            System.out.println ("    Packets sent: " + stats.getSentPacketCount());
            System.out.println ("    Bytes sent: " + stats.getSentByteCount());
            System.out.println ("    Packets received: " + stats.getReceivedPacketCount());
            System.out.println ("    Bytes received: " + stats.getReceivedByteCount());
            System.out.println ("    Retransmits: " + stats.getRetransmittedPacketCount());
            System.out.println ("    Packets discarded: " + stats.getDiscardedPacketCount());

            long time = System.currentTimeMillis() - startTime;
            System.out.println ("Time to receive " + bytesRead + " bytes = " + time + " (" + (bytesRead * 1000 / time) + " bytes/sec)");
            
            saveStats (mocket, "receiver", time);
            mocket.close();
        } 
        else {
            System.out.println ("DataRecv: creating ServerSocket");
            ServerSocket servSocket = new ServerSocket (LOCAL_PORT);
            byte[] buffer = new byte[MessageMocket.getMaximumMTU()];
            
            /* wait for connection */
            Socket socket = servSocket.accept();
            System.out.println ("DataRecv: ServerSocket accepted a connection");
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            DataInputStream dis = new DataInputStream (is);
            
            /* read size of data to receive */
            long datasize = dis.readLong();
            System.out.println ("DataRecv: will read [" + datasize + "] bytes.");
            
            /* start receiving data */
            System.out.println ("DataRecv: start receiving Data");
            long bytesRead = 0;
            long startTime = System.currentTimeMillis();

            while (true) {
                long bytesToRead = Math.min ((long)buffer.length, datasize - bytesRead);
                if (bytesToRead == 0) {
                    break;
                }
                if ((i = is.read (buffer, 0, (int)bytesToRead)) >= 0) {
                    bytesRead += i;
                } else {
                    System.out.println ("DataRecv: EOF reached so closing connection");
                    break;
                }
            }

            /* send ack */
            BufferedWriter bw = new BufferedWriter (new OutputStreamWriter(os));
            bw.write ("OK\n");
            bw.flush();

            /* print and save statistics */
            long time = System.currentTimeMillis() - startTime;
            System.out.println ("Time to receive " + bytesRead + " bytes = " + time + " (" + (bytesRead * 1000 / time) + " bytes/sec)");
            saveStats (socket, "receiver", time);
            
            /* close connection */
            socket.close();
        }
    }

    private static void saveStats (MessageMocket mocket, String type, long txTime)
    {
        String outFile = "stats-" + type + ".txt";

        try {
            FileOutputStream fos = new FileOutputStream(outFile, true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            Mocket.Statistics stats = mocket.getStatistics();

            String line = "[" + System.currentTimeMillis() + "]\t"
                        + "\t" + txTime
                        + "\t" + stats.getSentPacketCount()
                        + "\t" + stats.getSentByteCount()
                        + "\t" + stats.getReceivedByteCount()
                        + "\t" + stats.getRetransmittedPacketCount()
                        + "\t" + stats.getDiscardedPacketCount()
                        ;

            bw.write(line);
            bw.newLine();
            bw.flush();

            bw.close();
            fos.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void saveStats (Socket socket, String type, long txTime)
    {
        String outFile = "stats-" + type + ".txt";

        try {
            FileOutputStream fos = new FileOutputStream(outFile, true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            String line = "[" + System.currentTimeMillis() + "]\t"
                        + "\t" + txTime
                        ;

            bw.write(line);
            bw.newLine();
            bw.flush();

            bw.close();
            fos.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static final int LOCAL_PORT = 4000;
    private static final String CHARSET = "US-ASCII";
}
/*
 * vim: et ts=4 sw=4
 */
