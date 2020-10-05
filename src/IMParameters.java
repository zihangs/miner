import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTreeReduceParameters;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMc.probabilities.Probabilities;
import org.processmining.plugins.InductiveMiner.mining.logs.XLifeCycleClassifier;
import org.processmining.plugins.inductiveminer2.framework.basecases.BaseCaseFinder;
import org.processmining.plugins.inductiveminer2.framework.cutfinders.CutFinder;
import org.processmining.plugins.inductiveminer2.framework.fallthroughs.FallThrough;
import org.processmining.plugins.inductiveminer2.framework.postprocessor.PostProcessor;
import org.processmining.plugins.inductiveminer2.loginfo.IMLog2IMLogInfo;
import org.processmining.plugins.inductiveminer2.loginfo.IMLogInfo;
import org.processmining.plugins.inductiveminer2.logs.IMLog;
import org.processmining.plugins.inductiveminer2.mining.MinerState;
import org.processmining.plugins.inductiveminer2.mining.MiningParametersAbstract;

import gnu.trove.set.TIntSet;

public class IMParameters extends MiningParametersAbstract{

	public IMLog getIMLog(XLog xLog) {
		// TODO Auto-generated method stub
		return null;
	}

	public XLifeCycleClassifier getLifeCycleClassifier() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasNoise() {
		// TODO Auto-generated method stub
		return false;
	}

	public Probabilities getSatProbabilities() {
		// TODO Auto-generated method stub
		return null;
	}

	public IMLog2IMLogInfo getLog2LogInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<BaseCaseFinder> getBaseCaseFinders() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<CutFinder> getCutFinders() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<FallThrough> getFallThroughs() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isRepairLifeCycles() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<PostProcessor> getPostProcessors() {
		// TODO Auto-generated method stub
		return null;
	}

	public EfficientTreeReduceParameters getReduceParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	public IMLog[] splitLogConcurrent(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState) {
		// TODO Auto-generated method stub
		return null;
	}

	public IMLog[] splitLogInterleaved(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState) {
		// TODO Auto-generated method stub
		return null;
	}

	public IMLog[] splitLogLoop(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState) {
		// TODO Auto-generated method stub
		return null;
	}

	public IMLog[] splitLogOr(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState) {
		// TODO Auto-generated method stub
		return null;
	}

	public IMLog[] splitLogSequence(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState) {
		// TODO Auto-generated method stub
		return null;
	}

	public IMLog[] splitLogXor(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState) {
		// TODO Auto-generated method stub
		return null;
	}

}
