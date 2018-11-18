package com.greatgaming;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class ConnectionTest extends TestCase {
    public ConnectionTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( ConnectionTest.class );
    }

    public void testConnectionThrowsIOException() throws Exception {
        DataHandler handler = mock(DataHandler.class);
        ServerSocket serverSocket = mock(ServerSocket.class);
        Socket connectionSocket = mock(Socket.class);
        when(connectionSocket.getInputStream()).thenThrow(new IOException());
        ConnectionPool pool = mock(ConnectionPool.class);
        when(serverSocket.accept()).thenReturn(connectionSocket);

        Connection conn = new Connection(handler, serverSocket, pool);
        conn.run();

        verify(connectionSocket, times(1)).close();
        verify(serverSocket, times(1)).close();
        verify(pool, times(1)).connectionClosed(conn);
    }

    public void testClientClosesConnection() throws Exception {
        DataHandler handler = mock(DataHandler.class);
        when(handler.handleData(anyString())).thenReturn("touched it");
        ServerSocket serverSocket = mock(ServerSocket.class);
        Socket connectionSocket = mock(Socket.class);
        when(connectionSocket.getInputStream()).thenReturn(new ByteArrayInputStream("null".getBytes()));
        when(connectionSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        ConnectionPool pool = mock(ConnectionPool.class);
        when(serverSocket.accept()).thenReturn(connectionSocket);

        Connection conn = new Connection(handler, serverSocket, pool);
        conn.run();

        verify(handler, times(1)).handleData("null");
        verify(connectionSocket, times(1)).close();
        verify(serverSocket, times(1)).close();
        verify(pool, times(1)).connectionClosed(conn);
    }
}
