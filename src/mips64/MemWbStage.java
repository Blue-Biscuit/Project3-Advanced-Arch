package mips64;

public class MemWbStage {

    PipelineSimulator simulator;
    boolean halted;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int aluIntData;
    int loadIntData;
    
    Register regB;
    Register regResult;
    
    Register regBOld;
    Register regResultOld;

    public MemWbStage(PipelineSimulator sim) {
        simulator = sim;
    }

    public boolean isHalted() {
        return halted;
    }

    public void update() {
        // Set the instruction values of this pipeline reg to the values of the 
        // previous (EX/MEM).
        regBOld = regB;
        regResultOld = regResult;
        
        instPC = simulator.exMem.instPC;
        opcode = simulator.exMem.opcode;
        aluIntData = simulator.exMem.aluIntData;
        regB = simulator.idEx.regB;
        regResult = simulator.idEx.regA;
        shouldWriteback = simulator.idEx.shouldWriteback;
        
        // Get loadIntData
        if (shouldWriteback) {

            loadMem();
            storeMem();
        
            // Write back to register.
            writeBack();
        }
        
    }
    
    private void loadMem() {
        if (opcode == Instruction.INST_LW && regB != null) {
            loadIntData = simulator.memory.getIntDataAtAddr(regB.getValue());
        }
    }
    
    private void storeMem() {
        if (opcode == Instruction.INST_SW) {
            int addr = simulator.exMem.aluIntData;
            
            simulator.memory.setIntDataAtAddr(addr, regB.getValue());
        }
    }
    
    private void writeBack() {
        int toWB;
        
        if (opcode == Instruction.INST_LW) {
            toWB = loadIntData;
        }
        else {
            toWB = aluIntData;
        }
        
        regResult.setValue(toWB);
    }
}
