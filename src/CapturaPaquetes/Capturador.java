/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CapturaPaquetes;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.Packet;

/**
 *
 * @author adrian
 */
public class Capturador {
    
    private NetworkInterface[] dispositivos;
    private JpcapCaptor capturador;
    private int dispositivoSeleccionado;
    
    public Capturador(){
        this.capturador = null;        
    }
    
    public NetworkInterface[] obtenerDispositivos(){
        this.dispositivos =JpcapCaptor.getDeviceList();        
        return this.dispositivos;
    }
    
    public void seleccionarDispositivo(int nDispositivo){
        try {
            this.capturador=JpcapCaptor.openDevice(this.dispositivos[1],65535,false,1000);
        } catch (IOException ex) {
            Logger.getLogger(Capturador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*public Packet conseguirPaquete(int ){        
        this.capturador.processPacket(1, new Receiver());
    }*/
    
}
