package mips64;

public class IdExStage {
    static final int STAGE_NUMBER = 2;

    PipelineSimulator simulator;
    boolean shouldWriteback = false;
    int instPC;
    int opcode;
    int regAData;
    int regBData;
    int immediate;
    
    Register regA;
    Register regB;
    Register regResult;

    Instruction instr;


    public IdExStage(PipelineSimulator sim) {
        simulator = sim;
    }

    int getIntRegister(int regNum) {
        return this.simulator.regFile.get("R" + regNum).getValue();
    }

    public void update() {
        // Set the instruction values of this pipeline reg to the values of the 
        // previous (IF/ID).
        shouldWriteback = simulator.ifId.shouldWriteback;
        instPC = simulator.ifId.instPC;
        opcode = simulator.ifId.opcode;
        instr = simulator.ifId.instr;
        
        // Load fields depending on the type of instruction format.
        if (instr instanceof ITypeInst) {
            ITypeInst iInst = (ITypeInst)instr;
            regAData = iInst.getRS();
            regBData = iInst.getRT();
            
            regA = this.simulator.regFile.get("R" + iInst.getRS());
            regB = this.simulator.regFile.get("R" + iInst.getRT());
            regResult = regB;
            
            regAData = regA.getValue();
            regBData = regB.getValue();
            
            immediate = iInst.getImmed();
        }
        else if (instr instanceof RTypeInst) {
            RTypeInst rInst = (RTypeInst)instr;
            regResult = this.simulator.regFile.get("R" + rInst.getRD());
            regA = this.simulator.regFile.get("R" + rInst.getRS());
            regB = this.simulator.regFile.get("R" + rInst.getRT());
            immediate = rInst.getShamt();
            
            regAData = regA.getValue();
            regBData = regB.getValue();
            
        }
        else if (instr instanceof JTypeInst) {
            JTypeInst jInst = (JTypeInst)instr;
            immediate = jInst.getOffset();
        }
    }
    
}
