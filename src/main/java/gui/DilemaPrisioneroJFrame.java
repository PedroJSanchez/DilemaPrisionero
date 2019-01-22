/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import agentes.AgentePolicia;
import dilemaPrisionero.elementos.Condenas;
import dilemaPrisionero.elementos.DilemaPrisionero;
import static util.Constantes.CASTIGO;
import static util.Constantes.NUM_RONDAS;
import static util.Constantes.PRIMO;
import static util.Constantes.PROB_FINAL;
import static util.Constantes.RECOMPENSA;
import static util.Constantes.TENTACION;

/**
 *
 * @author pedroj
 */
public class DilemaPrisioneroJFrame extends javax.swing.JFrame {
    private AgentePolicia myAgent;

    /**
     * Creates new form DilemaPrisioneroJFrame
     */
    public DilemaPrisioneroJFrame(AgentePolicia agent) {
        initComponents();
        myAgent = agent;
        this.setTitle(myAgent.getName());
        
        tentacion.setText(Integer.toString(TENTACION));
        recompensa.setText(Integer.toString(RECOMPENSA));
        castigo.setText(Integer.toString(CASTIGO));
        primo.setText(Integer.toString(PRIMO));
        rondas.setText(Integer.toString(NUM_RONDAS));
        probFinal.setText(Integer.toString(PROB_FINAL));
        numJugadores.setText("0");
    }
    
    public void activarNuevaPartida(int n) {
        numJugadores.setText(Integer.toString(n));
        nuevaPartida.setEnabled(true);
    }
    
    public void anularNuevaPartida(int n) {
        numJugadores.setText(Integer.toString(n));
        nuevaPartida.setEnabled(false);
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tentacion = new javax.swing.JTextField();
        recompensa = new javax.swing.JTextField();
        primo = new javax.swing.JTextField();
        castigo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        rondas = new javax.swing.JTextField();
        probFinal = new javax.swing.JTextField();
        nuevaPartida = new javax.swing.JButton();
        label = new javax.swing.JLabel();
        numJugadores = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Configuración del Juego: Dilema del Prisionero");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setText("Años de condena por traicionar:");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText("Años de condena por no traicionar:");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText("Años de condena por mútua traición:");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setText("Años de condena por ser traicionado:");

        tentacion.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tentacion.setText("jTextField1");

        recompensa.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        recompensa.setText("jTextField1");

        primo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        primo.setText("jTextField1");

        castigo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        castigo.setText("jTextField1");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setText("Mínimo de rondas del juego:");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setText("Probabilidad de finalización :");

        rondas.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        rondas.setText("jTextField1");

        probFinal.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        probFinal.setText("jTextField1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tentacion)
                    .addComponent(recompensa)
                    .addComponent(rondas))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(jLabel7)))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(probFinal)
                    .addComponent(castigo)
                    .addComponent(primo))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(155, 155, 155)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(tentacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(castigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(recompensa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(primo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(probFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rondas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        nuevaPartida.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        nuevaPartida.setText("Nueva Partida");
        nuevaPartida.setEnabled(false);
        nuevaPartida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevaPartidaActionPerformed(evt);
            }
        });

        label.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        label.setText("Jugadores encontrados: ");

        numJugadores.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        numJugadores.setText("jLabel8");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numJugadores)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(nuevaPartida)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nuevaPartida)
                    .addComponent(label)
                    .addComponent(numJugadores))
                .addGap(26, 26, 26))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void nuevaPartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevaPartidaActionPerformed
        // TODO add your handling code here:
        Condenas condenas = new Condenas();
        DilemaPrisionero configuracion = new DilemaPrisionero();
        
        // Recogemos del formulario los parámetros de configuración
        // del nuevo juego
        condenas.setTentacion(Integer.parseInt(tentacion.getText()));
        condenas.setRecompensa(Integer.parseInt(recompensa.getText()));
        condenas.setCastigo(Integer.parseInt(castigo.getText()));
        condenas.setPrimo(Integer.parseInt(primo.getText()));
        
        configuracion.setTiempoCondena(condenas);
        configuracion.setRondas(Integer.parseInt(rondas.getText()));
        configuracion.setProbabilidadFin(Integer.parseInt(probFinal.getText()));
        
        // Pedimos al agente que inicie una nueva partida
        myAgent.nuevaPartida(configuracion);
    }//GEN-LAST:event_nuevaPartidaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField castigo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel label;
    private javax.swing.JButton nuevaPartida;
    private javax.swing.JLabel numJugadores;
    private javax.swing.JTextField primo;
    private javax.swing.JTextField probFinal;
    private javax.swing.JTextField recompensa;
    private javax.swing.JTextField rondas;
    private javax.swing.JTextField tentacion;
    // End of variables declaration//GEN-END:variables
}
