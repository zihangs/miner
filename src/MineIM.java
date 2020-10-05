import java.io.File;
import java.io.IOException;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.framework.packages.PackageManager.Canceller;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTree;
import org.processmining.plugins.InductiveMiner.mining.logs.XLifeCycleClassifier;
import org.processmining.plugins.inductiveminer2.helperclasses.XLifeCycleClassifierIgnore;
import org.processmining.plugins.inductiveminer2.logs.IMLog;
import org.processmining.plugins.inductiveminer2.logs.IMLogImpl;
import org.processmining.plugins.inductiveminer2.mining.InductiveMiner;
import org.processmining.plugins.inductiveminer2.plugins.EfficientTree2AcceptingPetriNetPlugin;

import au.edu.qut.prom.helpers.ConsoleUIPluginContext;
import au.edu.qut.prom.helpers.HeadlessDefinitelyNotUIPluginContext;

public class MineIM {
	private XEventClassifier transitionClassifier;
	private PluginContext context;
	private Canceller canceller;
	private String outputFileName;
	private ParametersIMImpl parameters;
	private XLifeCycleClassifier lifeCycleClassifier;
	private IMLog imlog;
	
	// input with XLog
	public MineIM(XLog log, String outputFileName) {
		this.transitionClassifier = new XEventNameClassifier();
		this.context = new HeadlessDefinitelyNotUIPluginContext(new ConsoleUIPluginContext(), "spn_dot_converter");
		this.canceller = new myCanceller();
		this.outputFileName = outputFileName;
		this.parameters = new ParametersIMImpl();
		this.lifeCycleClassifier = new XLifeCycleClassifierIgnore();
		this.imlog = new IMLogImpl(log, transitionClassifier, lifeCycleClassifier);
	}
	
	public void mine(float noiseThreshold) throws IOException {
		// set NoiseThreshold
		parameters.setNoiseThreshold(noiseThreshold);
		
		// core part: mine etree
		EfficientTree etree = InductiveMiner.mineEfficientTree(imlog, parameters, canceller);
		
		// convert etree to petri net
		EfficientTree2AcceptingPetriNetPlugin converter = new EfficientTree2AcceptingPetriNetPlugin();
		AcceptingPetriNet pn = converter.convertAndReduce(context, etree);
		// export
		File output = new File(outputFileName);
		pn.exportToFile(context, output);
		System.out.println("MineIM: " + outputFileName + "    Success!");
	}

}
