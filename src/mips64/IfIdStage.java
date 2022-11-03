package mips64;

public class IfIdStage {
    static final int STAGE_NUMBER = 1;
    
    PipelineSimulator simulator;
    int instPC;
    int opcode;
    Instruction instr;

    boolean shouldWriteback = false;

  


    public IfIdStage(PipelineSimulator sim) {
      simulator = sim;
    }

    public void update() {
        // Get the correct PC value.
        instPC = simulator.pc.getPC();
        shouldWriteback = true;

        // Load from program memory the instruction.
        instr = simulator.memory.getInstAtAddr(instPC);
        opcode = instr.getOpcode();
    }
}
