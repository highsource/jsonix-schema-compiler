package org.hisrc.jsonix.log;

public class SystemLog implements Log {

	@Override
	public void trace(Object message) {
		System.out.println(message);
	}

	@Override
	public void debug(Object message) {
		System.out.println(message);
	}
	
	@Override
	public void warn(Object message) {
		System.err.println(message);
	}
	
	@Override
	public void error(Object message) {
		System.err.println(message);
	}

}
