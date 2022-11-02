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
        regB = simulator.exMem.regB;
        regResult = simulator.exMem.regResult;
        shouldWriteback = simulator.exMem.shouldWriteback;
        
        // Get loadIntData
        if (shouldWriteback) {

            loadMem();
            storeMem();
            if (writesBack()) {
                writeBack();
            }
            resolveBranch();
        }
        
    }
    
    private void loadMem() {
        if (opcode == Instruction.INST_LW && regB != null) {
            loadIntData = simulator.memory.getIntDataAtAddr(aluIntData);
        }
    }
    
    private void storeMem() {
        if (opcode == Instruction.INST_SW) {
            simulator.memory.setIntDataAtAddr(aluIntData, regB.getValue());
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
    
    /**
     * Resolves a branching instruction.
     */
    private void resolveBranch() {
        if (simulator.exMem.branching) {
            System.out.println("Branch was taken.");
            // "Squash" the previous instructions.
            simulator.idEx.shouldWriteback = false;
            simulator.ifId.shouldWriteback = false;
            
            // Load the new PC value.
            simulator.pc.setPC(aluIntData - 4); // -4, b/c the PC will incr. on update.
        }
        else if (isBranching(opcode)) {
            System.out.println("Branch was not taken.");
        }
    }
    
    private boolean isBranching(int opcode) {
        return (opcode == Instruction.INST_BEQ) 
                || (opcode == Instruction.INST_BNE)
                || (opcode == Instruction.INST_BLTZ)
                || (opcode == Instruction.INST_BLEZ)
                || (opcode == Instruction.INST_BGEZ)
                || (opcode == Instruction.INST_BGTZ)
                || (opcode == Instruction.INST_J)
                || (opcode == Instruction.INST_JR)
                || (opcode == Instruction.INST_JALR)
                || (opcode == Instruction.INST_JAL);
    }
    
    /**
     * True if the opcode is an instruction which writes to register.
     * @return 
     */
    private boolean writesBack() {
        return !(isBranching(opcode) || opcode == Instruction.INST_NOP || opcode == Instruction.INST_HALT);
    }
}
