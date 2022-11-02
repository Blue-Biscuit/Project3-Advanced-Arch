/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package mips64;

/**
 *
 * @author ahuffman
 */
public class InvalidRegisterException extends RuntimeException {

    /**
     * Creates a new instance of <code>InvalidRegisterException</code> without
     * detail message.
     */
    public InvalidRegisterException() {
    }

    /**
     * Constructs an instance of <code>InvalidRegisterException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidRegisterException(String msg) {
        super(msg);
    }
}
