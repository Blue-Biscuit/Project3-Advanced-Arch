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
        
        switch (opcode) {
            case Instruction.INST_ADD:
            case Instruction.INST_ADDI:
            case Instruction.INST_BEQ:
            case Instruction.INST_BNE:
            case Instruction.INST_BLTZ:
            case Instruction.INST_BLEZ:
            case Instruction.INST_BGEZ:
            case Instruction.INST_BGTZ:
            case Instruction.INST_JR:
            case Instruction.INST_JAL:
            case Instruction.INST_JALR:
            case Instruction.INST_J:
            case Instruction.INST_LW:
            case Instruction.INST_SW:
                aluIntData = aluTopInput(opcode) + aluBottomInput(opcode);
                break;
            case Instruction.INST_SUB:
                aluIntData = aluTopInput(opcode) - aluBottomInput(opcode);
                break;
            case Instruction.INST_MUL:
                aluIntData = aluTopInput(opcode) * aluBottomInput(opcode);
                break;
            case Instruction.INST_DIV:
                aluIntData = aluTopInput(opcode) / aluBottomInput(opcode);
                break;
            case Instruction.INST_AND:
            case Instruction.INST_ANDI:
                aluIntData = aluTopInput(opcode) & aluBottomInput(opcode);
                break;
            case Instruction.INST_OR:
            case Instruction.INST_ORI:
                aluIntData = aluTopInput(opcode) | aluBottomInput(opcode);
                break;
            case Instruction.INST_XOR:
            case Instruction.INST_XORI:
                aluIntData = aluTopInput(opcode) ^ aluBottomInput(opcode);
                break;
            case Instruction.INST_SLL:
                aluIntData = aluTopInput(opcode) << aluBottomInput(opcode);
                break;
            case Instruction.INST_SRL:
                aluIntData = aluTopInput(opcode) >>> aluBottomInput(opcode);
                break;
            case Instruction.INST_SRA:
                aluIntData = aluTopInput(opcode) >> aluBottomInput(opcode);
                break;
            default:
                System.out.println("UNIMPLEMENTED! AHHHH!!!!");
                break;
        }
    }
    
    public boolean branchWasTaken() {
        return false; // TODO: unimplemented.
    }
    
    /**
     * Gets the "top" input to the ALU, either PC + 4 or a register.
     * @return 
     */
    private int aluTopInput(int opcode) {
        if (isBranching(opcode)) {
            return simulator.idEx.instPC + 4;
        }
        else {
            return simulator.idEx.regAData;
        }
    }
    
    /**
     * Gets the "bottom" input to the ALU, either a register or an immediate.
     * @return 
     */
    private int aluBottomInput(int opcode) {
        if (isImmediate(opcode)) {
            return simulator.idEx.immediate;
        }
        else {
            return simulator.idEx.regBData;
        }
    }
    
    /**
     * True if an instruction is an immediate instruction.
     */
    private boolean isImmediate(int opcode) {
        return (opcode == Instruction.INST_ADDI) 
                || (opcode == Instruction.INST_ANDI) 
                || (opcode == Instruction.INST_ORI)
                || (opcode == Instruction.INST_XOR);
    }
    
    private boolean isBranching(int opcode) {
        return (opcode == Instruction.INST_BEQ) 
                || (opcode == Instruction.INST_BNE)
                || (opcode == Instruction.INST_BLTZ)
                || (opcode == Instruction.INST_BLEZ)
                || (opcode == Instruction.INST_BGEZ)
                || (opcode == Instruction.INST_BGTZ);
    }
    
}
