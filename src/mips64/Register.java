/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mips64;

/**
 * A single integer register in the register file.
 *
 * @author ahuffman
 */
public class Register {

    private String name;
    private int value;

    /**
     * The pipeline stage which has reserved this register for writing. 0 if the
     * register has not been reserved.
     */
    private int reservedBy = 0;

    /**
     * Initializes a register.
     *
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
     *
     * @param name The name of the register.
     */
    public Register(String name) {
        this(name, 0);
    }

    /**
     * Gets the name of the register.
     *
     * @return The name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the value of the register.
     *
     *
     * @return The value.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Sets the value of the register.
     *
     * @param value The value.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Returns the ID of the pipeline stage which has reserved this register.
     *
     * @return The integer identifier of the pipeline stage which is currently
     * writing this register.
     */
    public int reservedBy() {
        return reservedBy;
    }

    /**
     * Reserves the register for writing.
     *
     * @param stage The pipeline stage reserving the register
     */
    public void reserve(int stage) {
        reservedBy = stage;
    }

    /**
     * Increments the reservation by 1, for when a reserved register is
     * transferred between stages.
     */
    public void incReservation() {
        reservedBy++;
    }

    /**
     * True if this register has been reserved for writing.
     *
     * @return True if this register has been reserved for writing.
     */
    public boolean isReserved() {
        return reservedBy != 0;
    }

    /**
     * "Un-reserves" the register for writing, once a dependency has cleared.
     */
    public void dereserve() {
        reservedBy = 0;
    }

    /**
     * Clones a register instance.
     *
     * @return The clone.
     */
    public Register clone() {
        Register c = new Register(this.name, this.value);
        c.reserve(this.reservedBy);
        return c;
    }
}
