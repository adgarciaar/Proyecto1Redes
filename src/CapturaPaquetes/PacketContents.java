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

    public static TCPPacket tcp;
    public static UDPPacket udp;
    public static ICMPPacket icmp;
    public static ARPPacket arp;

    public static List<Object[]> listaAtributosPaquetes = new ArrayList<Object[]>();
    public static List<EthernetPacket> listaEthernet = new ArrayList<>();
    
    @Override
    public void receivePacket(Packet packet) {        
        
        DefaultTableModel model = (DefaultTableModel) AnalizadorPaquetes.jTablePaquetes.getModel();
        double estimatedTime = (double)System.currentTimeMillis() - AnalizadorPaquetes.tiempoInicio;
        estimatedTime = estimatedTime/1000;
        
        listaEthernet.add((EthernetPacket) packet.datalink);
        
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

            Object[] row = {AnalizadorPaquetes.numeroPaquete, estimatedTime, arp.getSenderHardwareAddress(), 
                arp.getTargetHardwareAddress(), "ARP", packet.len};
            listaAtributosPaquetes.add(new Object[]{AnalizadorPaquetes.numeroPaquete, arp.len, arp.sender_hardaddr, 
                arp.sender_protoaddr,arp.target_hardaddr, arp.target_protoaddr,"ARP", arp.header,arp.data});           

            model.addRow(row);
            AnalizadorPaquetes.numeroPaquete++;
        }else{
            System.out.println("se nos fue uno"+packet.toString());            
        }
    }
}
