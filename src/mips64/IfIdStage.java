package mips64;

public class IfIdStage {
  PipelineSimulator simulator;
  int instPC;
  int opcode;
  Instruction instr;


  public IfIdStage(PipelineSimulator sim) {
    simulator = sim;

  }

  public void update() {
      // Get the correct PC value.
      instPC = simulator.pc.getPC();
      
      // Load from program memory the instruction.
      instr = simulator.memory.getInstAtAddr(instPC);
      opcode = instr.getOpcode();
  }
}
