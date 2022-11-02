package mips64;

public class IdExStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int regAData;
    int regBData;
    int immediate;
    
    Register regA;
    Register regB;

    public IdExStage(PipelineSimulator sim) {
        simulator = sim;
    }

    int getIntRegister(int regNum) {
        // todo - add supporting code
        return this.simulator.regFile.get("R" + regNum).getValue();
    }

    public void update() {
        // Set the instruction values of this pipeline reg to the values of the 
        // previous (IF/ID).
        instPC = simulator.ifId.instPC;
        opcode = simulator.ifId.opcode;
    }
}
