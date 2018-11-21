package com.greatgaming.server;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ConsoleHandlerTest extends TestCase {
    public ConsoleHandlerTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( ConsoleHandlerTest.class );
    }

    public void testConsoleHandlerTest() {
        ConsoleHandler handler = new ConsoleHandler();
		String result = handler.handleData("test");
		assert("test".equals(result));
    }
}