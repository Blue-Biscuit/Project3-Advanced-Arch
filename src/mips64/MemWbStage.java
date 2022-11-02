package mips64;

public class MemWbStage {

    PipelineSimulator simulator;
    boolean halted;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int aluIntData;
    int loadIntData;

    public MemWbStage(PipelineSimulator sim) {
        simulator = sim;
    }

    public boolean isHalted() {
        return halted;
    }

    public void update() {
        // Set the instruction values of this pipeline reg to the values of the 
        // previous (EX/MEM).
        instPC = simulator.exMem.instPC;
        opcode = simulator.exMem.opcode;
        aluIntData = simulator.exMem.aluIntData;
        
        // Get loadIntData
        loadMem();
        storeMem(opcode);
        
    }
    
    private void loadMem() { // TODO: figure out how to write to int regs.
        if (opcode == Instruction.INST_LW) {
            int regData = simulator.idEx.regBData;
            loadIntData = simulator.memory.getIntDataAtAddr(regData);
        }
    }
    
    private void storeMem(int opcode) {
        if (opcode == Instruction.INST_SW) {
            int toStoreRegVal = simulator.idEx.regBData;
            int addr = simulator.exMem.aluIntData;
            
            simulator.memory.setIntDataAtAddr(addr, toStoreRegVal);
        }
    }
}
