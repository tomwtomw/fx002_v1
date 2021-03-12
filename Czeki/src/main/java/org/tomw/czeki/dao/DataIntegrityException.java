/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.dao;

/**
 *
 * @author tomw
 */

public class DataIntegrityException extends Exception {
    public DataIntegrityException(String message) {
        super(message);
    }
}