/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.imageview;

import org.tomw.czeki.dao.DataFormatException;

/**
 *
 * @author tomw
 */
public class CheckImageException extends DataFormatException{
    public CheckImageException(String message) {
        super(message);
    }
}
