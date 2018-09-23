/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CapturaPaquetes;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

/**
 *
 * @author adrian
 */
public class Capturador {
    
    public static NetworkInterface[] obtenerDispositivos(){
        NetworkInterface[] dispositivos =JpcapCaptor.getDeviceList();        
        return dispositivos;
    }
    
}
