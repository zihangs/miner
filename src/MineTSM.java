import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.plugins.transitionsystem.converter.util.TSConversions;
import org.processmining.plugins.transitionsystem.miner.TSMiner;
import org.processmining.plugins.transitionsystem.miner.TSMinerInput;
import org.processmining.plugins.transitionsystem.miner.TSMinerOutput;
import org.processmining.plugins.transitionsystem.miner.TSMinerTransitionSystem;
import org.processmining.plugins.transitionsystem.miner.modir.TSMinerModirInput;
import org.processmining.plugins.transitionsystem.miner.util.TSAbstractions;
import org.processmining.plugins.transitionsystem.miner.util.TSDirections;
import org.processmining.plugins.tsml.exporting.TsmlExportTS;

import au.edu.qut.prom.helpers.ConsoleUIPluginContext;
import au.edu.qut.prom.helpers.HeadlessDefinitelyNotUIPluginContext;
import au.edu.qut.prom.helpers.HeadlessUIPluginContext;

public class MineTSM {
	private XEventClassifier transitionClassifier;
	private Collection<XEventClassifier> classifiers;
	private PluginContext context;
	private UIPluginContext uicontext;
	private XLog log;
	private String outputFileName;
	
	// input with XLog
	public MineTSM(XLog log, String outputFileName) {
		this.transitionClassifier = XLogInfoImpl.NAME_CLASSIFIER;
		this.classifiers = new ArrayList<XEventClassifier>();
		classifiers.add(transitionClassifier);
		this.context = new HeadlessDefinitelyNotUIPluginContext(new ConsoleUIPluginContext(), "spn_dot_converter");
		this.uicontext = new HeadlessUIPluginContext(new ConsoleUIPluginContext(), "spmrunner_logparser");
		// this.canceller = new myCanceller();
		this.log = log;
		this.outputFileName = outputFileName;
	}
	
	public void mine(int states_num, int top_percentage_clf, int top_percentage_label, int timeout) 
			throws IOException, InterruptedException, ExecutionException {
		
		// wrap in a thread: setting parameters and mining (with a time limit)
		final Runnable stuffToDo = new Thread() {
			  @Override 
			  public void run() { 
				  TSMiner tsm = new TSMiner(context);
				  TSMinerInput tsm_input = new TSMinerInput(context, log, classifiers, transitionClassifier);
				  //TSDirections direction = TSDirections.BACKWARD;
				  // fixed
				  TSAbstractions abstraction = TSAbstractions.SET;
				  // select directions and classifiers (here we only choose one direction and one classifier)
				  // add a loop to iterate all transitionClassifier
				  tsm_input.getModirSettings(TSDirections.BACKWARD, transitionClassifier).setUse(true);
				  tsm_input.getModirSettings(TSDirections.FORWARD, transitionClassifier).setUse(true);
				  // no limit (all events)
				  // a for loop: check if modirSettings.getUse() == true
				  for (TSDirections direction : TSDirections.values()) {
					  for (XEventClassifier classifier : tsm_input.getClassifiers()) {
						  TSMinerModirInput modirSettings = tsm_input.getModirSettings(direction, classifier);
						  if (modirSettings.getUse()) {
							  modirSettings.setAbstraction(abstraction);
							  modirSettings.setFilteredHorizon(-1);   // -1 or (0 - 100)
							  modirSettings.setHorizon(-1);
						  }
					  }
				  } 
					
				  // no limit (all states)
				  tsm_input.setMaxStates(states_num);   // -1 or (0 - 1000)
					
				  // Configure key classifier filter
				  tsm_input = classifierFilter(tsm_input, top_percentage_clf);
				  // Configure label filter
				  tsm_input = labelFilter(tsm_input, top_percentage_label);
					
				  //the last few clicks
				  tsm_input.getConverterSettings().setUse(TSConversions.KILLSELFLOOPS, false);
				  tsm_input.getConverterSettings().setUse(TSConversions.EXTEND, false);
				  tsm_input.getConverterSettings().setUse(TSConversions.MERGEBYINPUT, true);
				  tsm_input.getConverterSettings().setUse(TSConversions.MERGEBYOUTPUT, true);
				  tsm_input.setAddArtificialStates(true);
					
				  //  MINING 
				  TSMinerOutput tsm_output = tsm.mine(tsm_input);
				  // convert
				  TSMinerTransitionSystem ts = tsm_output.getTransitionSystem();
				  // export
				  TsmlExportTS exportTool = new TsmlExportTS();
				  File outputFile = new File(outputFileName);
				  try {
					  exportTool.export(uicontext, ts, outputFile);
				  } catch (IOException e) {
				  	  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  System.out.println("MineTSML: " + outputFileName + "    Success!");
			  }
		    };
			
		    
		    // run the thread for timeout
			final ExecutorService executor = Executors.newSingleThreadExecutor();
			final Future future = executor.submit(stuffToDo);
			executor.shutdown(); // This does not cancel the already-scheduled task.
		
			try { 
				  future.get(timeout, TimeUnit.SECONDS); 
				}
				catch (InterruptedException ie) { 
				  /* Handle the interruption. Or ignore it. */ 
				}
				catch (ExecutionException ee) { 
				  /* Handle the error. Or ignore it. */ 
				}
				catch (TimeoutException te) {
					future.cancel(true);
					System.out.println("interrupted");
				  /* Handle the timeout. Or ignore it. */ 
				}
				if (!executor.isTerminated())
				    executor.shutdownNow(); // If you want to stop the code that hasn't finished.
	}
	
	

	// Config classifier filter
	// Param: percentage (0-100) - int
	public TSMinerInput classifierFilter(TSMinerInput tsm_input, int percentage) {
		XLogInfo info = tsm_input.getLogInfo();
		Collection<String> s = new TreeSet<String>();
		Map<String, Integer> m = new HashMap<String, Integer>();

		for (XEventClass eventClass : info.getEventClasses(transitionClassifier).getClasses()) {
			s.add(eventClass.toString());
			m.put(eventClass.toString(), eventClass.size());
		}
		
		int size = 0;
		TreeSet<Integer> eventSizes = new TreeSet<Integer>();
		for (String event : s) {
			size += m.get(event);
			eventSizes.add(m.get(event));
		}
		
		javax.swing.JList nameFilter = new javax.swing.JList(s.toArray());
		nameFilter.setSelectionInterval(0, info.getEventClasses(transitionClassifier).getClasses().size() - 1);
		
		// the number of events to include
		int treshold = size * percentage;
		int value = 0;
		
		nameFilter.clearSelection();
		while (100 * value < treshold) {
			int eventSize = eventSizes.last();
			eventSizes.remove(eventSize);
			int index = 0;
			for (String event : s) {
				if (m.get(event) == eventSize) {
					value += eventSize;
					nameFilter.addSelectionInterval(index, index);
				}
				index++;
			}
		}
		
		for (TSDirections direction : TSDirections.values()) {
			tsm_input.getModirSettings(direction, transitionClassifier).getFilter().clear();
			for (Object object : nameFilter.getSelectedValues()) {
				if (tsm_input.getModirSettings(direction, transitionClassifier).getUse()) {
					tsm_input.getModirSettings(direction, transitionClassifier).getFilter().add(object.toString());
				}
			}
		}
		return tsm_input;
	}
	
	// Config label filter
	// Param: percentage (0-100) - int
	public TSMinerInput labelFilter(TSMinerInput tsm_input, int percentage) {
		XLogInfo info = tsm_input.getLogInfo();
		Collection<String> s = new TreeSet<String>();
		Map<String, Integer> m = new HashMap<String, Integer>();
		
		for (XEventClass eventClass : info.getEventClasses().getClasses()) {
			s.add(eventClass.toString());
			m.put(eventClass.toString(), eventClass.size());
		}
		
		javax.swing.JList nameFilter = new javax.swing.JList(s.toArray());
		nameFilter.setSelectionInterval(0, info.getEventClasses().getClasses().size() - 1);
		
		int size = 0;
		TreeSet<Integer> eventSizes = new TreeSet<Integer>();
		for (String event : s) {
			size += m.get(event);
			eventSizes.add(m.get(event));
		}
		int treshold = size * percentage;
		int value = 0;
		nameFilter.clearSelection();
		while (100 * value < treshold) {
			int eventSize = eventSizes.last();
			eventSizes.remove(eventSize);
			int index = 0;
			for (String event : s) {
				if (m.get(event) == eventSize) {
					value += eventSize;
					nameFilter.addSelectionInterval(index, index);
				}
				index++;
			}
		}
		
		tsm_input.getVisibleFilter().clear();
		for (Object object : nameFilter.getSelectedValues()) {
			tsm_input.getVisibleFilter().add(object.toString());
		}
		return tsm_input;
	}
	
}
