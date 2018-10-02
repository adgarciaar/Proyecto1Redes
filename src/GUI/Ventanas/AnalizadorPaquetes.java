/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Ventanas;

import CapturaPaquetes.PacketContents;
import CapturaPaquetes.jpcap_thread;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

/**
 *
 * @author adrian
 */
public class AnalizadorPaquetes extends javax.swing.JFrame {

    public static NetworkInterface[] listaInterfaces; //lista de las interfaces de red
    public static JpcapCaptor capturador; //capturador de paquetes de jpcap
    jpcap_thread hilo; //hilo para ejecutar la captura de paquetes
    public static int indiceInterface; //número de la interface de red seleccionada
    boolean estadoCaptura; //estado de captura de paquetes
    public static int numeroPaquete; //numero de paquete para JTable
    private final JFrame ventanaInicio; //ventana de inicio en caso de regresar
    public static double tiempoInicio; //tiempo de inicio de captura (time del sistema)

    /**
     * Creates new form AnalizadorMensajes
     *
     * @param ventanaInicio
     */
    public AnalizadorPaquetes(JFrame ventanaInicio) {
        initComponents();
        //inicialización variables
        indiceInterface = 0; //interface               
        estadoCaptura = false;
        numeroPaquete = 1;
        listaInterfaces = JpcapCaptor.getDeviceList();

        this.ventanaInicio = ventanaInicio;
        //obtener las interfaces de red detectadas y listarlas en el combobox
        listaInterfaces = JpcapCaptor.getDeviceList();
        int numeroDispositivo = -1;
        for (NetworkInterface dispositivo : listaInterfaces) {
            numeroDispositivo += 1;
            this.jComboBoxInterfaces.addItem(numeroDispositivo + ". " + dispositivo.name
                    + dispositivo.description + "-" + dispositivo.datalink_name
                    + "-" + dispositivo.datalink_description);
        }
    }

    public void CapturePackets() {
        //se crea hilo que realiza la captura continuamente
        hilo = new jpcap_thread() {

            @Override
            public Object construct() {
                try {
                    //se capturan paquetes en interface seleccionada en modo no promiscuo refrescando cada 1000 ms
                    capturador = JpcapCaptor.openDevice(listaInterfaces[indiceInterface], 65535, false, 1000);
                    //inicializar tiempo con el time del sistema
                    tiempoInicio = (double) System.currentTimeMillis();
                    while (estadoCaptura) { //mientras no se presione detener captura
                        //se capturan paquetes que se muestran en jtable y se guardan en una lista
                        capturador.processPacket(1, new PacketContents());
                    }
                    //se cierra el capturador al presionar detener captura
                    capturador.close();

                } catch (IOException e) {
                    System.out.print(e);
                }
                return 0;
            }

            @Override
            public void finished() {
                this.interrupt();
            }
        };

        hilo.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButtonCapturar = new javax.swing.JButton();
        jButtonDetener = new javax.swing.JButton();
        jComboBoxInterfaces = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jButtonRegresar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaDetalles = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTablePaquetes = new javax.swing.JTable(){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Captura de mensajes");

        jButtonCapturar.setText("Empezar captura");
        jButtonCapturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCapturarActionPerformed(evt);
            }
        });

        jButtonDetener.setText("Detener captura");
        jButtonDetener.setEnabled(false);
        jButtonDetener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDetenerActionPerformed(evt);
            }
        });

        jComboBoxInterfaces.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxInterfacesActionPerformed(evt);
            }
        });

        jLabel2.setText("Interfaces:");

        jButtonRegresar.setText("Regresar");
        jButtonRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegresarActionPerformed(evt);
            }
        });

        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });

        jTextAreaDetalles.setColumns(20);
        jTextAreaDetalles.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextAreaDetalles.setRows(5);
        jTextAreaDetalles.setEnabled(false);
        jScrollPane2.setViewportView(jTextAreaDetalles);

        jTablePaquetes.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTablePaquetes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Número", "Tiempo (s)", "Fuente", "Destino", "Protocolo", "Longitud (bytes)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTablePaquetes.setRowHeight(20);
        jTablePaquetes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablePaquetesMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTablePaquetes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonRegresar)
                .addGap(195, 195, 195)
                .addComponent(jButtonSalir)
                .addGap(415, 415, 415))
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBoxInterfaces, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonCapturar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonDetener, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3))
                            .addComponent(jLabel1))))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCapturar)
                    .addComponent(jComboBoxInterfaces, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jButtonDetener))
                .addGap(23, 23, 23)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonRegresar)
                    .addComponent(jButtonSalir))
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCapturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCapturarActionPerformed
        //se limpia el JTable y se inicializa numeroPaquete desde 1
        DefaultTableModel model = (DefaultTableModel) jTablePaquetes.getModel();
        model.setRowCount(0);
        numeroPaquete = 1;
        //se limpian las listas de paquetes
        PacketContents.listaEthernet.clear();
        PacketContents.listaAtributosPaquetes.clear();
        //se limpian el JTextArea
        jTextAreaDetalles.setText("");
        //se habilitan y deshabilitan los respectivos componentes
        jButtonCapturar.setEnabled(false);
        jButtonDetener.setEnabled(true);
        jComboBoxInterfaces.setEnabled(false);
        //se extrae el número de interface seleccionado para realizar captura
        String datosInterface = (String) jComboBoxInterfaces.getSelectedItem();
        int pointIndex = datosInterface.indexOf(".");
        int idInterface = Integer.parseInt(datosInterface.substring(0, pointIndex));
        indiceInterface = idInterface;
        //se inicia la captura de paquetes iniciando el hilo
        estadoCaptura = true;
        CapturePackets();
    }//GEN-LAST:event_jButtonCapturarActionPerformed

    private void jButtonDetenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDetenerActionPerformed
        //se detiene la captura de paquetes finalizando el hilo
        estadoCaptura = false;
        hilo.finished();
        //se habilitan y deshabilitan los respectivos componentes
        jButtonCapturar.setEnabled(true);
        jButtonDetener.setEnabled(false);
        jComboBoxInterfaces.setEnabled(true);
    }//GEN-LAST:event_jButtonDetenerActionPerformed

    private void jButtonRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegresarActionPerformed
        if (estadoCaptura == true) { //en caso de estar capturando se detiene hilo
            estadoCaptura = false;
            hilo.finished();
        }
        //regreso a la ventana de inicio
        this.dispose();
        this.ventanaInicio.setVisible(true);
    }//GEN-LAST:event_jButtonRegresarActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        System.exit(0); //finalizar ejecución del programa
    }//GEN-LAST:event_jButtonSalirActionPerformed
    //evento de seleccionar un paquete en el JTable
    private void jTablePaquetesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePaquetesMouseClicked
        jTextAreaDetalles.setText(""); //limpiar el JTextArea
        //conseguir el número de paquete seleccionado
        Object obj = jTablePaquetes.getModel().getValueAt(jTablePaquetes.getSelectedRow(), 0);
        int nPaquete = (int) obj;
        nPaquete -= 1;
        //extracción de los datos del paquete Ethernet
        String macFuente = PacketContents.listaEthernet.get(nPaquete).getSourceAddress();
        String macDestino = PacketContents.listaEthernet.get(nPaquete).getDestinationAddress();
        //revisar el tipo de tipo Ethernet
        String infoCompletaEthernet = PacketContents.listaEthernet.get(nPaquete).toString();
        int indicePrimerParentesis = infoCompletaEthernet.indexOf('(');
        int indiceUltimoParentesis = infoCompletaEthernet.indexOf(')');
        int ethernetType = Integer.parseInt(infoCompletaEthernet.substring(indicePrimerParentesis + 1, indiceUltimoParentesis));
        String tipoEthernet = "";
        switch (ethernetType) {
            case 2048:
                tipoEthernet = "IP (0x0800)";
                break;
            case 512:
                tipoEthernet = "PUP (0x0200)";
                break;
            case 2054:
                tipoEthernet = "ARP (0x0806)";
                break;
            case 32821:
                tipoEthernet = "RARP (ARP reverso) (0x8035)";
                break;
            case 33024:
                tipoEthernet = "IEEE 802.1Q VLAN (0x8100)";
                break;
            case 34525:
                tipoEthernet = "IPv6 (0x86dd)";
                break;
            case 36864:
                tipoEthernet = "Loop (0x9000)";
                break;
        }
        //dependiendo del tipo de paquete se imprimen sus atributos
        if (PacketContents.listaAtributosPaquetes.get(nPaquete)[4] == "TCP") {

            boolean reservado = (boolean) PacketContents.listaAtributosPaquetes.get(nPaquete)[16];
            boolean urg = (boolean) PacketContents.listaAtributosPaquetes.get(nPaquete)[17];
            boolean psh = (boolean) PacketContents.listaAtributosPaquetes.get(nPaquete)[18];
            boolean rst = (boolean) PacketContents.listaAtributosPaquetes.get(nPaquete)[19];
            boolean syn = (boolean) PacketContents.listaAtributosPaquetes.get(nPaquete)[20];
            boolean fin = (boolean) PacketContents.listaAtributosPaquetes.get(nPaquete)[21];
            
            int reservadoInt;
            int urgInt;
            int pshInt;
            int rstInt;
            int synInt;
            int finInt;
            
            short punteroUrgente = (short) PacketContents.listaAtributosPaquetes.get(nPaquete)[23];
                 
            if (reservado == true)
                reservadoInt = 1;
            else
                reservadoInt = 0;
            
            if (urg == true)
                urgInt = 1;
            else
                urgInt = 0;
            
            if (psh == true)
                pshInt = 1;
            else
                pshInt = 0;
            
            if (rst == true)
                rstInt = 1;
            else
                rstInt = 0;
            
            if (syn == true)
                synInt = 1;
            else
                synInt = 0;
            
            if (fin == true)
                finInt = 1;
            else
                finInt = 0;
            
            jTextAreaDetalles.setText(
                    //información del paquete
                    "Paquete " + (nPaquete + 1)
                    + "\n\tTiempo de llegada: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[14]
                    + "\n\tLongitud: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[1] + " bytes"
                    //información Ethernet
                    + "\n"        
                    + "\nEthernet"
                    + "\n\tFuente: " + macFuente
                    + "\n\tDestino: " + macDestino
                    + "\n\tProtocolo: " + tipoEthernet
                    //información IP
                    + "\n"
                    + "\nInternet Protocol Version 4"
                    + "\n\tVersión: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[15]
                    + "\n\tLongitud Header: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[12] + " bytes"
                    + "\n\tLongitud Data: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[9] + " bytes"
                    + "\n\tProtocolo: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[4]
                    + " (" + PacketContents.listaAtributosPaquetes.get(nPaquete)[13] + ")"
                    + "\n\tIP fuente: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[2]
                    + "\n\tIP destino: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[3]
                    //información TCP
                    + "\n"
                    + "\nTransmission Control Protocol"
                    + "\n\tPuerto fuente: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[5]
                    + "\n\tPuerto destino: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[6]
                    + "\n\tSecuencia No: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[10]
                    + "\n\tAck: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[7]
                    + "\n\tAck No: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[8]
                    + "\n\tOffset: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[11]
                    + "\n\tFlag: "        
                    + "\n\t\tReservado: " + reservadoInt
                    + "\n\t\tURG: " + urgInt
                    + "\n\t\tPSH: " + pshInt
                    + "\n\t\tRST: " + rstInt
                    + "\n\t\tSYN: " + synInt
                    + "\n\tVentana: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[22]
                    + "\n\tPuntero urgente: " + punteroUrgente
            );

        } else if (PacketContents.listaAtributosPaquetes.get(nPaquete)[4] == "UDP") {

            jTextAreaDetalles.setText(
                    //información del paquete
                    "Paquete " + (nPaquete + 1)
                    + "\n\tTiempo de llegada: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[11]
                    + "\n\tLongitud: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[1] + " bytes"
                    //información Ethernet
                    + "\nEthernet"
                    + "\n\tFuente: " + macFuente
                    + "\n\tDestino: " + macDestino
                    + "\n\tProtocolo: " + tipoEthernet
                    //información IP
                    + "\nInternet Protocol Version 4"
                    + "\n\tVersión: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[12]
                    + "\n\tLongitud Header: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[9] + " bytes"
                    + "\n\tLongitud Data: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[7] + " bytes"
                    + "\n\tProtocolo: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[4]
                    + " (" + PacketContents.listaAtributosPaquetes.get(nPaquete)[10] + ")"
                    + "\n\tIP fuente: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[2]
                    + "\n\tIP destino: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[3]
                    //información UDP
                    + "\nUser Datagram Protocol"
                    + "\n\tPuerto fuente: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[5]
                    + "\n\tPuerto destino: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[6]
                    + "\n\tOffset: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[8]
            );

        } else if (PacketContents.listaAtributosPaquetes.get(nPaquete)[4] == "ICMP") {

            byte[] byteDataICMP = (byte[]) PacketContents.listaAtributosPaquetes.get(nPaquete)[8];
            String stringDataICMP = new String(byteDataICMP);

            short shortChecksum = (short) PacketContents.listaAtributosPaquetes.get(nPaquete)[5];
            String intHexStringChecksum = Integer.toHexString(shortChecksum);
            String shortHexStringChecksum = "0x" + intHexStringChecksum.substring(4);

            short shortSecuencia = (short) PacketContents.listaAtributosPaquetes.get(nPaquete)[15];
            String intHexStringSecuencia = Integer.toHexString(shortSecuencia);
            String shortHexStringSecuencia = "0x" + intHexStringSecuencia.substring(4);

            jTextAreaDetalles.setText(
                    //información del paquete
                    "Paquete " + (nPaquete + 1)
                    + "\n\tTiempo de llegada: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[10]
                    + "\n\tLongitud: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[1] + " bytes"
                    + "\n"
                    //información Ethernet
                    + "\nEthernet"
                    + "\n\tFuente: " + macFuente
                    + "\n\tDestino: " + macDestino
                    + "\n\tProtocolo: " + tipoEthernet
                    + "\n"
                    //información IP
                    + "\nInternet Protocol Version 4"
                    + "\n\tVersión: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[12]
                    + "\n\tLongitud Header: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[6] + " bytes"
                    + "\n\tLongitud Data: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[11] + " bytes"
                    + "\n\tProtocolo: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[4]
                    + " (" + PacketContents.listaAtributosPaquetes.get(nPaquete)[9] + ")"
                    + "\n\tIP fuente: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[2]
                    + "\n\tIP destino: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[3]
                    //información ICMP
                    + "\n"
                    + "\nInternet Control Message Protocol"
                    + "\n\tTipo: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[13]
                    + "\n\tCódigo: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[14]
                    + "\n\tChecksum: " + shortHexStringChecksum
                    + "\n\tNúmero de secuencia: " + shortHexStringSecuencia
                    + "\n\tIdentificador: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[16]
                    + "\n\tOffset: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[7]
                    + "\n\tData: " + stringDataICMP
            );

        } else if (PacketContents.listaAtributosPaquetes.get(nPaquete)[6] == "ARP") {

            jTextAreaDetalles.setText(
                    //información del paquete
                    "Paquete " + (nPaquete + 1)
                    + "\n\tTiempo de llegada: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[10]
                    + "\n\tLongitud: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[1] + " bytes"
                    + "\n\tLongitud Header: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[7] + " bytes"
                    + "\n\tLongitud Data: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[8] + " bytes"
                    //información Ethernet
                    + "\nEthernet"
                    + "\n\tFuente: " + macFuente
                    + "\n\tDestino: " + macDestino
                    + "\n\tProtocolo: " + tipoEthernet
                    //información ARP
                    + "\nAddress Resolution Protocol"
                    + "\n\tProtocolo: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[6]
                    + "\n\tTipo: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[9]
                    + "\n\tMAC fuente: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[2]
                    + "\n\tIP fuente: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[3]
                    + "\n\tMAC destino: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[4]
                    + "\n\tIP destino: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[5]
            );
        } else {
            jTextAreaDetalles.setText(
                    //información del paquete
                    "Paquete " + (nPaquete + 1)
                    + "\n\tTiempo de llegada: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[9]
                    + "\n\tLongitud: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[1] + " bytes"
                    //información Ethernet
                    + "\nEthernet"
                    + "\n\tFuente: " + macFuente
                    + "\n\tDestino: " + macDestino
                    + "\n\tProtocolo: " + tipoEthernet
                    //información IP
                    + "\nInternet Protocol Version 4"
                    + "\n\tVersión: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[10]
                    + "\n\tLongitud Header: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[5] + " bytes"
                    + "\n\tLongitud Data: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[7] + " bytes"
                    + "\n\tProtocolo: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[8]
                    + " (" + PacketContents.listaAtributosPaquetes.get(nPaquete)[4] + ")"
                    + "\n\tIP fuente: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[2]
                    + "\n\tIP destino: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[3]
                    //información protocolo
                    + "\nProtocol"
                    + "\n\tOffset: " + PacketContents.listaAtributosPaquetes.get(nPaquete)[6]
            );
        }

    }//GEN-LAST:event_jTablePaquetesMouseClicked

    private void jComboBoxInterfacesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxInterfacesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxInterfacesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCapturar;
    private javax.swing.JButton jButtonDetener;
    private javax.swing.JButton jButtonRegresar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JComboBox<String> jComboBoxInterfaces;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    public static javax.swing.JTable jTablePaquetes;
    private javax.swing.JTextArea jTextAreaDetalles;
    // End of variables declaration//GEN-END:variables
}
