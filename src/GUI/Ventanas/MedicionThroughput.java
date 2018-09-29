/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Ventanas;

import GUI.Odometro.Gauge.Radial;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import MedicionThroughput.Throughput;
import java.util.ArrayList;

/**
 *
 * @author adrian
 */
public class MedicionThroughput extends javax.swing.JFrame implements ActionListener {

    private final JFrame ventanaInicio; //ventana de inicio en caso de regresar
    private final Radial odometroRecibido; //odometro para tráfico recibido
    private final Radial odometroEnviado; //odometro para tráfico enviado

    //contador para conocer si recien se arrancó la medición
    //nInterface para guardar interface de red seleccionada para medición
    private int contador, nInterface; 
    //recibidoAntes y enviadoAntes guardan #bytes medidos 1 segundo antes para calcular throughput
    //diferenciaRecibido y diferenciaEnviado guardan diferencia del #bytes respecto a 1 s después 
    private long recibidoAntes, enviadoAntes, diferenciaRecibido, diferenciaEnviado;
    //se guardan ancho de banda (capac. máxima) y el throughput de data recibida y enviada
    private double anchoBanda, throughputEnviado, throughputRecibido;
    private final Throughput medidorTrafico; //objeto para extraer información de tráfico

    /**
     * Creates new form MedicionThroughput
     *
     * @param ventanaInicio
     */
    public MedicionThroughput(JFrame ventanaInicio) {
        initComponents();
        //inicializar variables
        this.ventanaInicio = ventanaInicio;
        this.medidorTrafico = new Throughput();
        //obtener interfaces de red para medición y listarlas en el combobox
        medidorTrafico.listarInterfaces();
        ArrayList<String> listaInterfaces = medidorTrafico.getListaInterfaces();
        int numeroDispositivo = 0;
        for (String dispositivo : listaInterfaces) {
            numeroDispositivo += 1;
            this.jComboBoxInterfaces.addItem(numeroDispositivo + ". " + dispositivo);
        }
        //inializar variables y odómetro
        nInterface = 0;        
        this.odometroRecibido = new Radial();
        this.odometroRecibido.setTitle("Recibido");
        this.odometroRecibido.setUnitString("Mbps");
        //this.gaugeRecibido.setMaxValue(this.anchoBanda);
        this.odometroRecibido.setMaxValue(100);
        this.jPanel1.setLayout(new BorderLayout());
        this.jPanel1.add(this.odometroRecibido, BorderLayout.CENTER);

        this.odometroEnviado = new Radial();
        this.odometroEnviado.setTitle("Enviado");
        this.odometroEnviado.setUnitString("Mbps");
        //this.gaugeEnviado.setMaxValue(this.anchoBanda);
        this.odometroEnviado.setMaxValue(100);
        this.jPanel2.setLayout(new BorderLayout());
        this.jPanel2.add(this.odometroEnviado, BorderLayout.CENTER);

        this.pack();

        this.contador = 0;
        this.throughputRecibido = 0;
        this.throughputEnviado = 0;
    }
    //extrae ancho de banda y calcula throughput actualizando el odómetro
    public void medirTrafico(){
        if (nInterface != 0) { //si ya se seleccionó interface de red
            this.contador++;
            if (this.contador == 1) { //si se empieza la medición se adquieren primeros datos
                this.medidorTrafico.run();
                this.recibidoAntes = medidorTrafico.getBytesRecibidos();
                this.enviadoAntes = medidorTrafico.getBytesEnviados();
            }
            if (this.contador > 1) { //ya se había iniciado la medición anteriormente
                //se adquieren datos actuales y se comparan con lo de hace 1 segundo
                this.contador = 2;
                this.recibidoAntes = medidorTrafico.getBytesRecibidos();
                this.enviadoAntes = medidorTrafico.getBytesEnviados();
                this.medidorTrafico.run();
                //se obtiene la diferencia en bytes con respecto a 1 segundo antes
                this.diferenciaRecibido = medidorTrafico.getBytesRecibidos() - this.recibidoAntes;
                this.diferenciaEnviado = medidorTrafico.getBytesEnviados() - this.enviadoAntes;
                //se obtiene el throughput en Mbps convirtiendo la diferencia en bytes
                //como se actualiza cada 1 s, la medida ya está en Mb/s
                this.throughputRecibido = (double) (diferenciaRecibido * 8) / 1000000;
                this.throughputEnviado = (double) (diferenciaEnviado * 8) / 1000000;
            }
            //si el ancho de banda se detecta como 0 se deja por default igual a 100 Mbps
            if ((double) medidorTrafico.getAnchoBanda() / 1000000 == 0) {
                this.anchoBanda = 100;
            } else {
                //ancho de banda que viene en bits/s se convierte a Mbps
                this.anchoBanda = (double) medidorTrafico.getAnchoBanda() / 1000000;
            }
            //establecer ancho de banda como valor máximo en odómetro
            this.jLabelAnchoBanda.setText("Ancho de banda: " + round(this.anchoBanda, 2) + " Mbps");
            this.odometroRecibido.setMaxValue(this.anchoBanda);
            this.odometroEnviado.setMaxValue(this.anchoBanda);
            this.odometroRecibido.setValueAnimated(throughputRecibido);
            this.odometroEnviado.setValueAnimated(throughputEnviado);
            //si throughput >1 Mbps se deja esta unidad, si es >1 Kbps y <1 Mbps
            //se muestra en Kbps, y si <1 Kbps se muestra en bytes
            if (this.throughputRecibido < 1) {
                if (this.throughputRecibido < 0.001) {
                    this.jLabelRecibido.setText("Recibido: " + round(throughputRecibido * 1000000, 2) + " bytes");
                } else {
                    this.jLabelRecibido.setText("Recibido: " + round(throughputRecibido * 1000, 2) + " Kbps");
                }
            } else {
                this.jLabelRecibido.setText("Recibido: " + round(throughputRecibido, 2) + " Mbps");
            }
            if (this.throughputEnviado < 1) {
                if (this.throughputEnviado < 0.001) {
                    this.jLabelEnviado.setText("Recibido: " + round(throughputEnviado * 1000000, 2) + " bytes");
                } else {
                    this.jLabelEnviado.setText("Enviado: " + round(throughputEnviado * 1000, 2) + " Kbps");
                }
            } else {
                this.jLabelEnviado.setText("Enviado: " + round(throughputEnviado, 2) + " Mbps");
            }
        }
    }
    
    //función que ejecuta el timer de ventana de inicio cada segundo para actualizar odómetro
    @Override
    public void actionPerformed(ActionEvent e) {
        medirTrafico();      
    }

    //redondea un double a dos cifras decimales
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        buttonRegresar = new javax.swing.JButton();
        buttonSalir = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabelEnviado = new javax.swing.JLabel();
        jLabelRecibido = new javax.swing.JLabel();
        jComboBoxInterfaces = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabelAnchoBanda = new javax.swing.JLabel();
        buttonMedir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setMinimumSize(new java.awt.Dimension(300, 300));
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 500));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 351, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("Throughtput de la red");

        buttonRegresar.setText("Regresar");
        buttonRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRegresarActionPerformed(evt);
            }
        });

        buttonSalir.setText("Salir");
        buttonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSalirActionPerformed(evt);
            }
        });

        jPanel2.setMinimumSize(new java.awt.Dimension(300, 300));
        jPanel2.setPreferredSize(new java.awt.Dimension(500, 500));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 351, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabelEnviado.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabelEnviado.setText("Enviado: 0.00 bytes");

        jLabelRecibido.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabelRecibido.setText("Recibido: 0.00 bytes");

        jLabel2.setText("Interfaces:");

        jLabelAnchoBanda.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabelAnchoBanda.setText("Ancho de banda: 0.0 Mbps");

        buttonMedir.setText("Medir");
        buttonMedir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMedirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(221, 221, 221)
                .addComponent(buttonRegresar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonSalir)
                .addGap(223, 223, 223))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(95, 95, 95)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(171, 171, 171)
                                .addComponent(jLabelRecibido)))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabelAnchoBanda)
                            .addGap(65, 65, 65)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabelEnviado)
                        .addGap(153, 153, 153))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(32, 32, 32)
                        .addComponent(jComboBoxInterfaces, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(buttonMedir)
                        .addGap(27, 27, 27))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addComponent(jComboBoxInterfaces, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(buttonMedir))
                .addGap(31, 31, 31)
                .addComponent(jLabelAnchoBanda)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEnviado)
                    .addComponent(jLabelRecibido))
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonRegresar)
                    .addComponent(buttonSalir))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSalirActionPerformed
        System.exit(0); //finalizar ejecución del programa
    }//GEN-LAST:event_buttonSalirActionPerformed

    private void buttonRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRegresarActionPerformed
        //cerrar esta ventana y regresar a la de inicio
        this.dispose();             
        this.ventanaInicio.setVisible(true);       
    }//GEN-LAST:event_buttonRegresarActionPerformed

    private void buttonMedirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMedirActionPerformed
        String datosInterface = (String) jComboBoxInterfaces.getSelectedItem();
        int pointIndex = datosInterface.indexOf(".");
        this.nInterface = Integer.parseInt(datosInterface.substring(0, pointIndex));
        medidorTrafico.setnInterface(nInterface);
        //reinicializar estos atributos en 0 para no obtener valores negativos de throughput
        this.contador = 0;
        this.enviadoAntes = 0;
        this.recibidoAntes = 0;
        this.throughputRecibido = 0;
        this.throughputEnviado = 0;
        this.odometroRecibido.setMaxValue(100);
        this.odometroEnviado.setMaxValue(100);
        //se empieza a medir tráfico
        medirTrafico();
    }//GEN-LAST:event_buttonMedirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonMedir;
    private javax.swing.JButton buttonRegresar;
    private javax.swing.JButton buttonSalir;
    private javax.swing.JComboBox<String> jComboBoxInterfaces;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelAnchoBanda;
    private javax.swing.JLabel jLabelEnviado;
    private javax.swing.JLabel jLabelRecibido;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
