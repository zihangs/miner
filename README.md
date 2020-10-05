# Process Mining Tools for Linux Terminals

The tools are built on top of ProM ([source code]( https://svn.win.tue.nl/repos/prom/)) and thanks for the help by Adam Burke and Dr Sander Leemans from QUT and Dr Eric Verbeek from TU/e. You can use the tool by downloading a runnable Jar from my google drive [here](https://drive.google.com/uc?export=download&id=1uiLXa8-5ReNkUxK_wKPUfbQcnIrNJr1c), and put this Jar file in the ``Workshop/`` directory. Or you can download the source code to your location machine and import the java project to your IDE for testing or further development.



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
java -cp miner.jar autoMiner -TSM example_15_short.xes output.tsml -1 100 100
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