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
        
        // Load fields depending on the type of instruction format.
        if (instr instanceof ITypeInst) {
            ITypeInst iInst = (ITypeInst)instr;
            regAData = iInst.getRS();
            regBData = iInst.getRT();
            immediate = iInst.getImmed();
        }
        else if (instr instanceof RTypeInst) {
            RTypeInst rInst = (RTypeInst)instr;
        }
    }
    
}
