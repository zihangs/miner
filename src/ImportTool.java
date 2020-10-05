import java.io.File;
import java.util.Collection;

import org.deckfour.xes.in.XUniversalParser;
import org.deckfour.xes.model.XLog;

public class ImportTool {
	
	private parserImpl parser;
	
	public ImportTool() {
		this.parser = new parserImpl();
	}
	
	public XLog readXES(String fileName) throws Exception {
		File file = new File(fileName);
		Collection<XLog> logs = parser.parse(file);
		return logs.iterator().next();
	}
	
	// Inner class for implement XUniversalParser
	private class parserImpl extends XUniversalParser{
		public parserImpl() {
			// pass
		}
	}
}
