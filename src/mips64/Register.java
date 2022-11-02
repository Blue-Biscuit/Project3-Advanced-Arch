/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mips64;

/**
 * A single integer register in the register file.
 * @author ahuffman
 */
public class Register {
    private String name;
    private int value;
    
    /**
     * Initializes a register.
     * @param name The name of the register.
     * @param value The 32-bit register value.
     */
    public Register(String name, int value) {
        if (name == null) {
            throw new NullPointerException("Parameter 'name' was null.");
        }
        
        this.name = name;
        this.value = value;
    }
    
    /**
     * Initializes a register with a zero-value.
     * @param name The name of the register.
     */
    public Register(String name) {
        this(name, 0);
    }
    
    /**
     * Gets the name of the register.
     * @return The name.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Gets the value of the register.
     * @return The value.
     */
    public int getValue() {
        return this.value;
    }
    
    /**
     * Sets the value of the register.
     * @param value The value.
     */
    public void setValue(int value) {
        this.value = value;
    }
}
