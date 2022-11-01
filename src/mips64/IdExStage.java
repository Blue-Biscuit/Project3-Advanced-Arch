package mips64;

public class IdExStage {

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int regAData;
    int regBData;
    int immediate;
    Instruction instr;
    int outALU; // The value which the ALU is outputting.

    public IdExStage(PipelineSimulator sim) {
        simulator = sim;
    }

    int getIntRegister(int regNum) {
        // todo - add supporting code
        return 0;
    }

    public void update() {
        // Set the instruction values of this pipeline reg to the values of the 
        // previous (IF/ID).
        instPC = simulator.ifId.instPC;
        opcode = simulator.ifId.opcode;
        instr = simulator.ifId.instr;
        
        // Switch by opcode.
        switch(opcode) {
            case Instruction.INST_ADDI:
                fetchOperandsImmediateArithmetic((ITypeInst)instr);
                outALU = regAData + immediate;
                break;
            case Instruction.INST_ANDI:
                fetchOperandsImmediateArithmetic((ITypeInst)instr);
                outALU = regAData & immediate;
                break;
            case Instruction.INST_ORI:
                fetchOperandsImmediateArithmetic((ITypeInst)instr);
                outALU = regAData | immediate;
                break;
            case Instruction.INST_XORI:
                fetchOperandsImmediateArithmetic((ITypeInst)instr);
                outALU = regAData ^ immediate;
                break;
        }
    }
    
    /**
     * Fetches the operands for an immediate arithmetic instruction.
     * @param instr The I-type instruction.
     */
    private void fetchOperandsImmediateArithmetic(ITypeInst instr) {
        regAData = instr.getRS();
        immediate = instr.getImmed();
        
        // TODO: forwarding logic
    }
}
