/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.entities;

import org.tomw.czeki.dao.DataFormatException;

/**
 *
 * @author tomw
 */
public class CsvDataFormatException extends DataFormatException{
    public CsvDataFormatException(String message) {
        super(message);
    }
}
