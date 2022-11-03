package mips64;

public class MemWbStage {

    static final int STAGE_NUMBER = 4;

    PipelineSimulator simulator;
    boolean halted;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int aluIntData;
    int loadIntData;
    
    int aluIntDataOld;
    int loadIntDataOld;
    int opcodeOld;
    boolean shouldWriteBackOld;
    
    boolean interlockVictimOld = false;
    boolean interlockVictim = false;
    
    Register regA;
    Register regB;
    Register regResult;

    Register regResultOld;

    public MemWbStage(PipelineSimulator sim) {
        simulator = sim;
    }

    public boolean isHalted() {
        return halted;
    }

    public void update() {
        
        // Copy last cycle's values into the "old" fields.
        shouldWriteBackOld = shouldWriteback;
        opcodeOld = opcode;
        loadIntDataOld = loadIntData;
        aluIntDataOld = aluIntData;
        regResultOld = regResult;
        interlockVictimOld = interlockVictim;
        
        // Clear reservations for old registers.
        if (regResultOld != null && !interlockVictimOld) {
            simulator.regFile.dereserve(regResultOld);
        }
        
        // Write back and update old values.
        if (shouldWriteBackOld) {
            if (writesBack(opcodeOld)) {
                int wb = writeBackVal(opcodeOld, loadIntDataOld, aluIntDataOld);
                regResultOld.setValue(wb);
            }
        }
        
        // Set the instruction values of this pipeline reg to the values of the 
        // previous (EX/MEM).
        instPC = simulator.exMem.instPC;
        opcode = simulator.exMem.opcode;
        aluIntData = simulator.exMem.aluIntData;
        regA = simulator.exMem.regA;
        interlockVictim = simulator.exMem.interlockVictim;
        if (regA != null) {
            regA = regA.clone();
            if (simulator.regFile.isReserved(regA)) {
                simulator.regFile.reserve(regA, STAGE_NUMBER);
            }
        }
        
        regB = simulator.exMem.regB;
        if (regB != null) {
            regB = regB.clone();
            if (simulator.regFile.isReserved(regB)) {
                simulator.regFile.reserve(regB, STAGE_NUMBER);
            }
        }
        
        regResult = simulator.exMem.regResult;
        if (regResult != null) {
            if (simulator.regFile.isReserved(regResult)) {
                simulator.regFile.reserve(regResult, STAGE_NUMBER);
            }
        }
        shouldWriteback = simulator.exMem.shouldWriteback;

        // Halt if halt
        if (opcode == Instruction.INST_HALT) {
            halted = true;
        }
        
        

        // Get loadIntData
        if (shouldWriteback) {
            loadMem();
            storeMem();
            
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

    private int writeBackVal(int opcode, int ldData, int aluData) {
        int toWB;

        if (opcode == Instruction.INST_LW) {
            toWB = ldData;
        } else {
            toWB = aluData;
        }

        return toWB;
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
            if (opcode == Instruction.INST_JR || opcode == Instruction.INST_JALR) {
                simulator.pc.setPC(regA.getValue());
            } else {
                simulator.pc.setPC(aluIntData);
            }

            if (opcode == Instruction.INST_JAL || opcode == Instruction.INST_JALR) {
                simulator.regFile.get("R31").setValue(instPC + 4);
            }
        } else if (isBranching(opcode)) {
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
     *
     * @return
     */
    private boolean writesBack(int opcode) {
        return !(isBranching(opcode)
                || opcode == Instruction.INST_NOP
                || opcode == Instruction.INST_HALT
                || opcode == Instruction.INST_SW);
    }
}
