# A Command Line Tool for Process Mining

The tool was built on top of ProM ([source code]( https://svn.win.tue.nl/repos/prom/)) and thanks for the help by Adam Burke and Dr Sander Leemans from QUT and Dr Eric Verbeek from TU/e. 

The ``miner.jar`` is the command line tool which contains mining functions. So you can mining process models by calling the ``miner.jar`` directly. Or you can check the source code and import the java project to the IDE on your local machine for testing or further development. Notice that, this tool was developed with java 8 runtime environment, so better to use java 8 to avoid issues.

### Miners and Commands

**Inductive Miner:** 

```sh
# java -cp miner.jar autoMiner <miner name> <input file> <output file> <noise threshold>
java -cp miner.jar autoMiner -IM example_15_short.xes output.pnml 0.0
```

ProM package source code for inductive miner can be found [here](https://svn.win.tue.nl/repos/prom/Packages/InductiveMiner/Trunk/src/org/processmining/plugins/inductiveminer2/plugins/).



**Directly Flow Miner:**

```sh
# java -cp miner.jar autoMiner <miner name> <input file> <output file> <noise threshold>
java -cp miner.jar autoMiner -DFM example_15_short.xes output.pnml 0.0
```

ProM package source code for directly flow miner can be found [here](https://svn.win.tue.nl/repos/prom/Packages/DirectlyFollowsModelMiner/Trunk/src/org/processmining/directlyfollowsmodelminer/mining/).



**Transition System Miner:**

```sh
# java -cp miner.jar autoMiner <miner name> <input file> <output file> <number of states> <classifier filter> <label filter>
java -cp miner.jar autoMiner -TSM example_15_short.xes output.pnml -1 100 100
```

ProM package source code for transition system miner can be found [here](https://svn.win.tue.nl/repos/prom/Packages/TransitionSystems/Trunk/src/org/processmining/plugins/).

Source code for parameter set up with UI: https://svn.win.tue.nl/repos/prom/Packages/TransitionSystems/Trunk/src/org/processmining/plugins/transitionsystem/miner/ui/TSMinerUI.java

Parameters settings for TS miner (build some as fixed, can be changed can recompiled if needed).

1. Fixed: Event Classifier, only use the Event Name (backward key and forward key)
2. Fixed: Collection of events, use Set as default
3. Fixed: Size of collection (events) set no limit
4. **Configurable:** Size of transition system (states), set to no limit as default, if we meet some issues later, then we can set a limit.
5. **Configurable:** Top percentage of event name (0 - 100%), try 100% first
6. **Configurable:** Top percentage of transition label (0 - 100%), try 100% first
7. Fixed: remove self loops = false / merge identical inflow = true / merge identical outflow = true / add start and end states = true