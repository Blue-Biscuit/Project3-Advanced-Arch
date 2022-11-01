package mips64;

public class ExMemStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int aluIntData;
    int storeIntData;

    public ExMemStage(PipelineSimulator sim) {
        simulator = sim;
    }

    public void update() {
        // Set the instruction values of this pipeline reg to the values of the 
        // previous (ID/EX).
        instPC = simulator.idEx.instPC;
        opcode = simulator.idEx.opcode;
    }
}
