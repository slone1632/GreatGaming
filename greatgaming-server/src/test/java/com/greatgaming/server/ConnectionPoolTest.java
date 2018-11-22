//package com.greatgaming.server;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class ConnectionPoolTest extends TestCase {
//    public ConnectionPoolTest( String testName ) {
//        super( testName );
//    }
//
//    public static Test suite() {
//        return new TestSuite( ConnectionPoolTest.class );
//    }
//
//    public void testStartPersistentClientConnection() throws Exception {
//        ConnectionFactory factory = mock(ConnectionFactory.class);
//        GameConnection conn = mock(GameConnection.class);
//        when(factory.build(
//                any(DataHandler.class),class
//                any(Integer.class),
//                any(ConnectionPool.class))).thenReturn(conn);
//        doNothing().when(factory).openConection(conn);
//
//        ConnectionPool pool = new ConnectionPool(2, factory);
//
//        Integer port = pool.startPersistentClientConnection(null);
//
//        assertEquals((Integer)6791, port);
//
//        port = pool.startPersistentClientConnection(null);
//        assertEquals((Integer)6790, port);
//    }
//
//    public void testConnectionClosed() throws Exception {
//        ConnectionFactory factory = mock(ConnectionFactory.class);
//        GameConnection conn = new GameConnection(null, null, null);
//        when(factory.build(
//                any(DataHandler.class),
//                any(Integer.class),
//                any(ConnectionPool.class))).thenReturn(conn);
//        doNothing().when(factory).openConection(conn);
//
//        ConnectionPool pool = new ConnectionPool(1, factory);
//
//        Integer port = pool.startPersistentClientConnection(mock(DataHandler.class));
//
//        assertEquals((Integer)6790, port);
//
//        pool.connectionClosed(conn);
//
//        port = pool.startPersistentClientConnection(null);
//        assertEquals((Integer)6790, port);
//    }
//}
