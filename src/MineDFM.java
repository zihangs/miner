import java.io.File;
import java.io.IOException;

import org.deckfour.xes.model.XLog;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.directlyfollowsmodelminer.mining.DFMMiner;
import org.processmining.directlyfollowsmodelminer.mining.variants.DFMMiningParametersDefault;
import org.processmining.directlyfollowsmodelminer.model.DirectlyFollowsModel;
import org.processmining.directlyfollowsmodelminer.model.DirectlyFollowsModel2AcceptingPetriNet;
import org.processmining.framework.packages.PackageManager.Canceller;
import org.processmining.framework.plugin.PluginContext;

import au.edu.qut.prom.helpers.ConsoleUIPluginContext;
import au.edu.qut.prom.helpers.HeadlessDefinitelyNotUIPluginContext;

public class MineDFM {
	private DFMMiningParametersDefault parameters;
	private PluginContext context;
	private Canceller canceller;
	private XLog log;
	private String outputFileName;
	
	// input with XLog
	public MineDFM(XLog log, String outputFileName) {
		this.canceller = new myCanceller();
		this.context = new HeadlessDefinitelyNotUIPluginContext(new ConsoleUIPluginContext(), "spn_dot_converter");
		this.parameters = new DFMMiningParametersDefault();
		this.log = log;
		this.outputFileName = outputFileName;
	}
	
	public void mine(double noiseThreshold) throws IOException {
		// set param
		parameters.setNoiseThreshold(noiseThreshold);
		DirectlyFollowsModel dfm = DFMMiner.mine(log, parameters, canceller);
		AcceptingPetriNet apn = DirectlyFollowsModel2AcceptingPetriNet.convert(dfm);
		File file = new File(outputFileName);
		apn.exportToFile(context, file);
		System.out.println("MineDFM: " + outputFileName + "    Success!");
	}

}
