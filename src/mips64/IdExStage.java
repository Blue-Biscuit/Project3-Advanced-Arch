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
    boolean interlockVictim = false;
    
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
            ITypeInst iInst = (ITypeInst) instr;
            regAData = iInst.getRS();
            regBData = iInst.getRT();

            regA = this.simulator.regFile.get("R" + iInst.getRS()).clone();
            regResult = this.simulator.regFile.get("R" + iInst.getRT());
            regB = regResult.clone();

            regAData = regA.getValue();
            regBData = regB.getValue();

            immediate = iInst.getImmed();
        } else if (instr instanceof RTypeInst) {
            RTypeInst rInst = (RTypeInst) instr;
            regResult = this.simulator.regFile.get("R" + rInst.getRD());
            regA = this.simulator.regFile.get("R" + rInst.getRS()).clone();
            regB = this.simulator.regFile.get("R" + rInst.getRT()).clone();
            immediate = rInst.getShamt();

            regAData = regA.getValue();
            regBData = regB.getValue();

        } else if (instr instanceof JTypeInst) {
            JTypeInst jInst = (JTypeInst) instr;
            immediate = jInst.getOffset();
        }
        
        if (doesInterlock()) {
            shouldWriteback = false;
            simulator.ifId.interlock = true;
            interlockVictim = true;
        }
        else {
            resolveDependencies();
        }
    }

    /**
     * Checks the conditions of interlock.
     *
     * @return True if the conditions are met for interlock.
     */
    private boolean doesInterlock() {
        // Check for interlock on reg A.
        if (regA != null) {
            int reservation = simulator.regFile.getReservation(regA);
            
            if (reservation == ExMemStage.STAGE_NUMBER && simulator.exMem.opcode == Instruction.INST_LW) {
                return true;
            }
        }
        
        // Check for interlock on reg B.
        else if (regB != null) {
            int reservation = simulator.regFile.getReservation(regB);

            if (reservation == ExMemStage.STAGE_NUMBER && simulator.exMem.opcode == Instruction.INST_LW) {
                return true;
            }
        }
        
        return false;
    }
    
    private void resolveDependencies() {
        resolveDependencies(regA);
        resolveDependencies(regB);
    }

    private void resolveDependencies(Register r) {
        if (r == null) {
            return;
        }
        
        int reservation = simulator.regFile.getReservation(r);
        
        if (reservation == ExMemStage.STAGE_NUMBER) {
            r.setValue(simulator.exMem.aluIntData);
        }
        else if (reservation == MemWbStage.STAGE_NUMBER) {
            if (simulator.memWb.opcode == Instruction.INST_LW) {
                r.setValue(simulator.memWb.loadIntData);
            }
            else {
                r.setValue(simulator.memWb.aluIntData);
            }
        }
    }
}
