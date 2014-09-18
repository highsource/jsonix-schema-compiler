package org.hisrc.jsonix.log;

public interface Log {

	public void trace(Object message);
	
	public void debug(Object message);
	
	public void warn(Object message);
}
