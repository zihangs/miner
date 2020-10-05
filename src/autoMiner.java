import org.deckfour.xes.model.XLog;

public class autoMiner {

	public static void main(String[] args) throws Exception {
		
		final int timeout = 6000;    //in seconds
		
		String minerName = args[0];
		String importFileName = args[1];
		String exportFileName = args[2];
		
		// Import Log
		ImportTool importTool = new ImportTool();
		XLog log = importTool.readXES(importFileName);
		
		switch (minerName) {
		case "-IM":
			// noiseThreshold: arg[3]
			float IMNoiseThreshold = Float.parseFloat(args[3]);
			MineIM im = new MineIM(log, exportFileName);
			im.mine(IMNoiseThreshold);
			break;
		case "-DFM":
			// noiseThreshold: arg[3]
			double DFMNoiseThreshold = Double.parseDouble(args[3]);
			MineDFM dfm = new MineDFM(log, exportFileName);
			dfm.mine(DFMNoiseThreshold);
			break;
		case "-TSM":
			MineTSM tsm = new MineTSM(log, exportFileName);
			// number of states: args[3]
			int states_num = Integer.parseInt(args[3]);
			// filter the top percentage of event name: args[4]
			int top_percentage_clf = Integer.parseInt(args[4]);
			// filter the top percentage of label: args[5]
			int top_percentage_label = Integer.parseInt(args[5]);
			tsm.mine(states_num, top_percentage_clf, top_percentage_label, timeout);
			break;
		default:
			System.out.println("unknown miner");
		}
		
	}
}
