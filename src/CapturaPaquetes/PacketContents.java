package CapturaPaquetes;


import GUI.Ventanas.AnalizadorPaquetes;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import javax.swing.table.DefaultTableModel;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;
import java.util.ArrayList;
import java.util.List;
import jpcap.packet.ARPPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.EthernetPacket;

public class PacketContents implements PacketReceiver {

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
        //dependiendo del tipo de paquete se añade su información principal en jtable
        //y se guardan sus atributos en la respectiva lista
        if (packet instanceof TCPPacket) {
            tcp = (TCPPacket) packet;

            Object[] row = {AnalizadorPaquetes.numeroPaquete, estimatedTime, tcp.src_ip, tcp.dst_ip, "TCP", packet.len};
            listaAtributosPaquetes.add(new Object[]{AnalizadorPaquetes.numeroPaquete, tcp.length, tcp.src_ip, tcp.dst_ip, "TCP", tcp.src_port, tcp.dst_port,
                tcp.ack, tcp.ack_num, tcp.data, tcp.sequence, tcp.offset, tcp.header, tcp.protocol});           
            
            model.addRow(row);
            AnalizadorPaquetes.numeroPaquete++;

        } else if (packet instanceof UDPPacket) {

            udp = (UDPPacket) packet;

            Object[] row = {AnalizadorPaquetes.numeroPaquete, estimatedTime, udp.src_ip, udp.dst_ip, "UDP",packet.len};
            listaAtributosPaquetes.add(new Object[]{AnalizadorPaquetes.numeroPaquete, udp.length, udp.src_ip, udp.dst_ip, "UDP", udp.src_port, udp.dst_port,
                udp.data, udp.offset, udp.header, udp.protocol});
            
            model.addRow(row);
            AnalizadorPaquetes.numeroPaquete++;

        } else if (packet instanceof ICMPPacket) {

            icmp = (ICMPPacket) packet;

            Object[] row = {AnalizadorPaquetes.numeroPaquete, estimatedTime, icmp.src_ip, 
                icmp.dst_ip, "ICMP",packet.len};
            listaAtributosPaquetes.add(new Object[]{AnalizadorPaquetes.numeroPaquete, icmp.length, icmp.src_ip, icmp.dst_ip, "ICMP", icmp.checksum, icmp.header,
                icmp.offset, icmp.orig_timestamp, icmp.recv_timestamp, icmp.trans_timestamp, icmp.data, icmp.protocol});
            
            model.addRow(row);
            AnalizadorPaquetes.numeroPaquete++;

        } else if (packet instanceof ARPPacket) {
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

            Object[] row = {AnalizadorPaquetes.numeroPaquete, estimatedTime, arp.getSenderHardwareAddress(), 
                arp.getTargetHardwareAddress(), "ARP", packet.len};
            listaAtributosPaquetes.add(new Object[]{AnalizadorPaquetes.numeroPaquete, arp.len, arp.sender_hardaddr, 
                arp.sender_protoaddr,arp.target_hardaddr, arp.target_protoaddr,"ARP", arp.header,arp.data,tipo});           

            model.addRow(row);
            AnalizadorPaquetes.numeroPaquete++;
        }
    }
}
