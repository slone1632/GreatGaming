package com.greatgaming.server;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class OpenNewConnectionHandlerTest extends TestCase
{
    public OpenNewConnectionHandlerTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( OpenNewConnectionHandlerTest.class );
    }

    public void testOpenNewConnectionHandler() throws Exception {
		ConnectionPool pool = mock(ConnectionPool.class);
		when(pool.startPersistentClientConnection(any(DataHandler.class))).thenReturn(3);
		
        OpenNewConnectionHandler handler = new OpenNewConnectionHandler(pool);
		
		String result = handler.handleData("anything");
		assert("3".equals(result));
    }

    public void testOpenNewConnectionHandlerException() throws Exception {
        ConnectionPool pool = mock(ConnectionPool.class);
        when(pool.startPersistentClientConnection(any(DataHandler.class))).thenThrow(new IOException());

        OpenNewConnectionHandler handler = new OpenNewConnectionHandler(pool);

        String result = handler.handleData("anything");
        assert("".equals(result));
    }
}