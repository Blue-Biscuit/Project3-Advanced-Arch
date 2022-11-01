/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mips64;

/**
 * An integer register on the architecture.
 * @author ahuffman
 */
public class Register {
    private String _name;
    private int _val;
    
    public Register(String name, int val) {
        this._name = name;
        this._val = val;
    }
    
    public String getName() {
        return _name;
    }
    
    public int getVal() {
        return _val;
    }
    
    public void setVal(int v) {
        _val = v;
    }
}
