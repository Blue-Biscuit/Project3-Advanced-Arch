/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mips64;

/**
 * A register file.
 *
 * @author ahuffman
 */
public class RegisterFile {

    private Register[] registers;
    private boolean[] inUse;

    private RegisterFile(Register[] registers) {
        // Null checking.
        if (registers == null) {
            throw new NullPointerException("Parameter 'registers' was null.");
        }
        for (int i = 0; i < registers.length; i++) {
            if (registers[i] == null) {
                throw new NullPointerException("Index " + i
                        + " of parameter 'registers' was null.");
            }
        }

        this.registers = registers;
        this.inUse = new boolean[registers.length];

        for (int i = 0; i < registers.length; i++) {
            this.inUse[i] = false;
        }
    }

    /**
     * Generates and returns the MIPS register file.
     *
     * @return The MIPS register file.
     */
    public static RegisterFile getMIPSRegisterFile() {
        final int MIPS_REGS_LEN = 32; // The number of regs in MIPS

        // Generate the registers.
        Register[] regs = new Register[MIPS_REGS_LEN];

        for (int i = 0; i < MIPS_REGS_LEN; i++) {
            regs[i] = new Register("R" + i);
        }

        // Call constructor.
        return new RegisterFile(regs);
    }

    /**
     * Gets a register by name.
     *
     * @param name The register name.
     * @return The register with the name.
     */
    public Register get(String name) {
        for (Register r : this.registers) {
            if (r.getName().equals(name)) {
                return r;
            }
        }

        throw new InvalidRegisterException("No register entitled '" + name + "'");
    }
}
