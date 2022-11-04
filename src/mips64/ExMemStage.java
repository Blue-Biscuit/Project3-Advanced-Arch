package mips64;

public class ExMemStage {

    static final int STAGE_NUMBER = 3;

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int aluIntData;
    int storeIntData;
    boolean interlockVictim = false;
    
    Register regA;
    Register regB;
    Register regResult;

    /**
     * True if the instruction is a taken branch.
     */
    boolean branching = false;

    public ExMemStage(PipelineSimulator sim) {
        simulator = sim;
    }

    public void update() {
        // Set the instruction values of this pipeline reg to the values of the 
        // previous (ID/EX).
        instPC = simulator.idEx.instPC;
        opcode = simulator.idEx.opcode;
        interlockVictim = simulator.idEx.interlockVictim;

        regA = simulator.idEx.regA;
        if (regA != null) {
            regA = regA.clone();
        }

        regB = simulator.idEx.regB;
        if (regB != null) {
            regB = regB.clone();
        }

        regResult = simulator.idEx.regResult;
        shouldWriteback = simulator.idEx.shouldWriteback;

        runALU();

        if (shouldWriteback) {
            branching = branchWasTaken();
            Register toReserve = getRegToClaim();
            
            if (toReserve != null) {
                simulator.regFile.reserve(toReserve, STAGE_NUMBER);
            }
        }
    }

    public boolean branchWasTaken() {

        switch (opcode) {
            case Instruction.INST_BEQ:
                return regA.getValue() == regB.getValue();
            case Instruction.INST_BNE:
                return regA.getValue() != regB.getValue();
            case Instruction.INST_BLTZ:
                return regA.getValue() < 0;
            case Instruction.INST_BLEZ:
                return regA.getValue() <= 0;
            case Instruction.INST_BGEZ:
                return regA.getValue() >= 0;
            case Instruction.INST_BGTZ:
                return regA.getValue() > 0;
            case Instruction.INST_J:
            case Instruction.INST_JR:
            case Instruction.INST_JAL:
            case Instruction.INST_JALR:
                return true;
            default:
                return false;
        }
    }

    void runALU() {
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

    /**
     * Gets the "top" input to the ALU, either PC + 4 or a register.
     *
     * @return
     */
    private int aluTopInput(int opcode) {

        if (isBranching(opcode)) {
            return instPC + 4;
        } else {
            return (regA == null) ? 0 : regA.getValue();
        }
    }

    /**
     * Gets the "bottom" input to the ALU, either a register or an immediate.
     *
     * @return
     */
    private int aluBottomInput(int opcode) {
        if (isImmediate(opcode)) {
            return simulator.idEx.immediate;
        } else {
            return (regB == null) ? 0 : regB.getValue();
        }
    }

    /**
     * True if an instruction is an immediate instruction.
     */
    private boolean isImmediate(int opcode) {
        return (opcode == Instruction.INST_ADDI)
                || (opcode == Instruction.INST_ANDI)
                || (opcode == Instruction.INST_ORI)
                || (opcode == Instruction.INST_XOR)
                || (opcode == Instruction.INST_SLL)
                || (opcode == Instruction.INST_SRL)
                || (opcode == Instruction.INST_SRA)
                || isBranching(opcode)
                || (opcode == Instruction.INST_LW)
                || (opcode == Instruction.INST_SW);
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
     * Gets the register which needs to be resereved by an instruction, if any.
     *
     * @return The register to claim, or null if no registers need to be
     * claimed.
     */
    private Register getRegToClaim() {
        boolean writes
                = !((opcode == Instruction.INST_SW)
                || (opcode == Instruction.INST_BEQ)
                || (opcode == Instruction.INST_BNE)
                || (opcode == Instruction.INST_BLTZ)
                || (opcode == Instruction.INST_BLEZ)
                || (opcode == Instruction.INST_BGEZ)
                || (opcode == Instruction.INST_BGTZ)
                || (opcode == Instruction.INST_J)
                || (opcode == Instruction.INST_JR)
                || (opcode == Instruction.INST_NOP)
                || (opcode == Instruction.INST_HALT));
        
        if (writes) {
            if (opcode == Instruction.INST_JAL || opcode == Instruction.INST_JALR) {
                return regA;
            }
            else {
                return regResult.clone();
            }
        }
        
        return null;
    }
}
