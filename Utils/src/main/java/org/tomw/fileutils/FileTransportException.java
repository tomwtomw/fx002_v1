/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.fileutils;

import java.io.IOException;

/**
 * Custom class for file transporter errors
 * @author tomw
 */
public class FileTransportException extends IOException {
   
    public FileTransportException(String message) {
        super(message);
    }
}

