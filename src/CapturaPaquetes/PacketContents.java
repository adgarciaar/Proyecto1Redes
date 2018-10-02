package CapturaPaquetes;


import GUI.Ventanas.AnalizadorPaquetes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import javax.swing.table.DefaultTableModel;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jpcap.packet.ARPPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;

public class PacketContents implements PacketReceiver {

    public static IPPacket ip; //paquete de tipo IP
    public static TCPPacket tcp; //paquete de tipo TCP
    public static UDPPacket udp; //paquete de tipo UDP
    public static ICMPPacket icmp; //paquete de tipo ICMP
    public static ARPPacket arp; //paquete de tipo ARP

    //lista para guardar los atributos de cada paquete
    public static List<Object[]> listaAtributosPaquetes = new ArrayList<Object[]>();
    //lista para guardar paquetes Ethernet
    public static List<EthernetPacket> listaEthernet = new ArrayList<>();
    
    @Override
    public void receivePacket(Packet packet) {       
        
        //se obtiene modelo actual mostrado en jTable de paquetes de AnalizadorPaquetes
        DefaultTableModel model = (DefaultTableModel) AnalizadorPaquetes.jTablePaquetes.getModel();
        
        //medir el tiempo en s de recepción del paquete respecto a cuando se empezó la captura
        double estimatedTime = (double)System.currentTimeMillis() - AnalizadorPaquetes.tiempoInicio;
        estimatedTime = estimatedTime/1000;
        
        //añadir paquete Ethernet a respectiva lista
        listaEthernet.add((EthernetPacket) packet.datalink);
        
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	Date date = new Date();	
        
        //dependiendo del tipo de paquete se añade su información principal en jtable
        //y se guardan sus atributos en la respectiva lista
        if(packet instanceof IPPacket) {
            
            //conversión de paquete a su paquete más específico
            ip = (IPPacket) packet;             
            if (packet instanceof TCPPacket) {
                //conversión de paquete a su paquete más específico
                tcp = (TCPPacket) packet;
                //crear row para el jtable
                Object[] row = {AnalizadorPaquetes.numeroPaquete, estimatedTime, 
                    tcp.src_ip, tcp.dst_ip, "TCP", packet.len};
                //adicionar objeto de atributos del paquete
                listaAtributosPaquetes.add(new Object[]{AnalizadorPaquetes.numeroPaquete, 
                    packet.len, tcp.src_ip, tcp.dst_ip, "TCP", tcp.src_port, tcp.dst_port,
                    tcp.ack, tcp.ack_num, tcp.data.length, tcp.sequence, tcp.offset, 
                    tcp.header.length, tcp.protocol, dateFormat.format(date),
                    ip.version });                 
                //adicionar row a jtable y aumentar en 1 el número de paquete
                model.addRow(row);
                AnalizadorPaquetes.numeroPaquete++;                

            } else if (packet instanceof UDPPacket) {
                //conversión de paquete a su paquete más específico
                udp = (UDPPacket) packet;
                //crear row para el jtable
                Object[] row = {AnalizadorPaquetes.numeroPaquete, estimatedTime, 
                    udp.src_ip, udp.dst_ip, "UDP",packet.len};
                //adicionar objeto de atributos del paquete
                listaAtributosPaquetes.add(new Object[]{AnalizadorPaquetes.numeroPaquete, 
                    packet.len, udp.src_ip, udp.dst_ip, "UDP", udp.src_port, udp.dst_port,
                    udp.data.length, udp.offset, udp.header.length, udp.protocol,
                    dateFormat.format(date),ip.version });
                //adicionar row a jtable y aumentar en 1 el número de paquete
                model.addRow(row);
                AnalizadorPaquetes.numeroPaquete++;

            } else if (packet instanceof ICMPPacket) {
                //conversión de paquete a su paquete más específico
                icmp = (ICMPPacket) packet;
                //crear row para el jtable
                Object[] row = {AnalizadorPaquetes.numeroPaquete, estimatedTime, 
                    icmp.src_ip, icmp.dst_ip, "ICMP",packet.len};
                //adicionar objeto de atributos del paquete
                listaAtributosPaquetes.add(new Object[]{AnalizadorPaquetes.numeroPaquete, 
                    packet.len, icmp.src_ip, icmp.dst_ip, "ICMP", icmp.checksum, icmp.header.length,
                    icmp.offset, icmp.data, icmp.protocol,dateFormat.format(date),
                    icmp.data.length, ip.version, icmp.type, icmp.code });
                
                //adicionar row a jtable y aumentar en 1 el número de paquete
                model.addRow(row);
                AnalizadorPaquetes.numeroPaquete++;
            } else {
                
                //asignar respectivo nombre al protocolo según su número
                String tipoProtocolo = "("+Short.toString(ip.protocol)+")";
                switch(ip.protocol){
                    case 2:
                        tipoProtocolo = "IGMP";
                        break;                    
                }
                //crear row para el jtable
                Object[] row = {AnalizadorPaquetes.numeroPaquete, estimatedTime, 
                    ip.src_ip, ip.dst_ip, tipoProtocolo, packet.len};
                //adicionar objeto de atributos del paquete
                listaAtributosPaquetes.add(new Object[]{AnalizadorPaquetes.numeroPaquete, 
                    packet.len, ip.src_ip, ip.dst_ip, ip.protocol, ip.header.length,
                    ip.offset, ip.data.length, tipoProtocolo,dateFormat.format(date),ip.version });
                
                //adicionar row a jtable y aumentar en 1 el número de paquete
                model.addRow(row);
                AnalizadorPaquetes.numeroPaquete++;                
            }
        }else if (packet instanceof ARPPacket) {
            //conversión de paquete a su paquete más específico
            arp = (ARPPacket) packet;
            //distinguir si es de tipo Request o Reply
            String tipoARP = arp.toString();            
            int firstIndex = tipoARP.indexOf("REQUEST");
            String tipo="";
            if(firstIndex==-1){
                tipo="Reply";
            }else{
                tipo="Request";
            }
            //crear row para el jtable
            Object[] row = {AnalizadorPaquetes.numeroPaquete, estimatedTime, 
                arp.getSenderHardwareAddress(), arp.getTargetHardwareAddress(), 
                "ARP", packet.len};
            //adicionar objeto de atributos del paquete
            listaAtributosPaquetes.add(new Object[]{AnalizadorPaquetes.numeroPaquete, 
                arp.len, arp.getSenderHardwareAddress(), arp.getSenderProtocolAddress(),
                arp.getTargetHardwareAddress(), arp.getTargetProtocolAddress(),
                "ARP", arp.header.length,arp.data.length, tipo,dateFormat.format(date) });           
            //adicionar row a jtable y aumentar en 1 el número de paquete
            model.addRow(row);
            AnalizadorPaquetes.numeroPaquete++;
        }
    }
}
