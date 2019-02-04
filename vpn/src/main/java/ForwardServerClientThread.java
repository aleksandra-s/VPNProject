/**
 * ForwardServerClientThread handles the clients of Nakov Forward Server. It
 * connects two sockets and starts the TCP forwarding between given client
 * and its assigned server. After the forwarding is failed and the two threads
 * are stopped, closes the sockets.
 * <p>
 * <p>
 * Modifications for IK2206:
 * - Server pool removed
 * - Two variants - client connects to listening socket or client is already connected
 * <p>
 * Peter Sjodin, KTH
 * <p>
 * Modifications for IK2206:
 * - Server pool removed
 * - Two variants - client connects to listening socket or client is already connected
 * <p>
 * Peter Sjodin, KTH
 */

/**
 * Modifications for IK2206:
 * - Server pool removed
 * - Two variants - client connects to listening socket or client is already connected
 *
 * Peter Sjodin, KTH
 */

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class ForwardServerClientThread extends Thread {
    private Socket mClientSocket = null;
    private Socket mServerSocket = null;
    private ServerSocket mListenSocket = null;
    private boolean mBothConnectionsAreAlive = false;
    private String mClientHostPort;
    private String mServerHostPort;
    private int mServerPort;
    private String mServerHost;

    private boolean server = false;

    /**
     * Creates a client thread for handling clients of NakovForwardServer.
     * A client socket should be connected and passed to this constructor.
     * A server socket is created later by run() method.
     */
    ForwardServerClientThread(Socket aClientSocket, String serverhost, int serverport) {
        mClientSocket = aClientSocket;
        mServerPort = serverport;
        mServerHost = serverhost;
    }

    /**
     * Creates a client thread for handling clients of NakovForwardServer.
     * Wait for client to connect on client listening socket.
     * A server socket is created later by run() method.
     */
    ForwardServerClientThread(ServerSocket listensocket, String serverhost, int serverport) throws IOException {
        mListenSocket = listensocket;
        mServerPort = serverport;
        mServerHost = serverhost;
    }

    public ServerSocket getListenSocket() {
        return mListenSocket;
    }

    /**
     * Obtains a destination server socket to some of the servers in the list.
     * Starts two threads for forwarding : "client in <--> dest server out" and
     * "dest server in <--> client out", waits until one of these threads stop
     * due to read/write failure or connection closure. Closes opened connections.
     *
     * If there is a listen socket, first wait for incoming connection
     * on the listen socket.
     */
    public void run() {
        try {

            // Wait for incoming connection on listen socket, if there is one 
            if (mListenSocket != null) {
                mClientSocket = mListenSocket.accept();
                mClientHostPort = mClientSocket.getInetAddress().getHostAddress() + ":" + mClientSocket.getPort();
                Logger.log("Accepted from  " + mServerPort + " <--> " + mClientHostPort + "  started.");
                server = true;
            } else {
                mClientHostPort = mClientSocket.getInetAddress().getHostAddress() + ":" + mClientSocket.getPort();
            }

            try {
                System.out.println("Server host: " + mServerHost);
                System.out.println("Server port: " + mServerPort);
                mServerSocket = new Socket(mServerHost, mServerPort);
            } catch (Exception e) {
                System.out.println("Connection failed to " + mServerHost + ":" + mServerPort);
                e.printStackTrace();
            }

            // Obtain input and output streams of server and client
            InputStream clientIn = mClientSocket.getInputStream();
            OutputStream clientOut = mClientSocket.getOutputStream();
            InputStream serverIn = mServerSocket.getInputStream();
            OutputStream serverOut = mServerSocket.getOutputStream();

            mServerHostPort = mServerHost + ":" + mServerPort;
            Logger.log("TCP Forwarding  " + mClientHostPort + " <--> " + mServerHostPort + "  started.");

            // Start forwarding of socket data between server and client
            ForwardThread clientForwarder;
            ForwardThread serverForwarder;
            boolean decryptData;
            if (server) {
                decryptData = true;
                clientForwarder = new ForwardThread(this, clientIn, serverOut, decryptData);
                decryptData = false;
                serverForwarder = new ForwardThread(this, serverIn, clientOut, decryptData);

            } else {
                //SWITCHED THE DECRYPTDATA VALUES, SHOULD NOT BE THE SAME
                decryptData = false;
                clientForwarder = new ForwardThread(this, clientIn, serverOut, decryptData);
                decryptData = true;
                serverForwarder = new ForwardThread(this, serverIn, clientOut, decryptData);
            }

            mBothConnectionsAreAlive = true;
            clientForwarder.start();
            serverForwarder.start();

        } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * connectionBroken() method is called by forwarding child threads to notify
     * this thread (their parent thread) that one of the connections (server or client)
     * is broken (a read/write failure occured). This method disconnects both server
     * and client sockets causing both threads to stop forwarding.
     */
    public synchronized void connectionBroken() throws IOException {
        if (mBothConnectionsAreAlive) {
            // One of the connections is broken. Close the other connection and stop forwarding
            // Closing these socket connections will close their input/output streams
            // and that way will stop the threads that read from these streams
            mServerSocket.close();
            mClientSocket.close();
            mBothConnectionsAreAlive = false;
            Logger.log("TCP Forwarding  " + mClientHostPort + " <--> " + mServerHostPort + "  stopped.");
        }
    }
}
