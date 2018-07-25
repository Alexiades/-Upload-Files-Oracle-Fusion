package Interface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainInterface {
    private JButton runButton;
    private JButton configurationButton;
    private JPanel Interfaz;
    static JFrame frame = new JFrame("MainInterface");
    static JFrame frame2 = new JFrame("Configuration");


    public MainInterface() {


        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
             //   inactivarBotones(ejecutarButton, empleadoCreadoButton, jobButton, ausenciasButton, ausenciaModificadaButton, asignacionCreadaButton, asignacionModificadaButton, contratoCreadoButton, contratoModificadoButton, empleadoModificadoButton, empleadoTerminadoButton, locationButton, positionButton, organizationButton, compensaciónVariableButton, compensacionfijaButton, nominaButton);
             //   Acciones.EmpleadoCreado(false);
            }
        });

        configurationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              //  inactivarBotones(ejecutarButton, empleadoCreadoButton, jobButton, ausenciasButton, ausenciaModificadaButton, asignacionCreadaButton, asignacionModificadaButton, contratoCreadoButton, contratoModificadoButton, empleadoModificadoButton, empleadoTerminadoButton, locationButton, positionButton, organizationButton, compensaciónVariableButton, compensacionfijaButton, nominaButton);
              //  Acciones.EmpleadoModificado(false);
                frame.setVisible(false);
                //Configuration conf = new Configuration();
                //conf.visible();
               // frame2.setSize(400,300);

                frame2.setContentPane(new Configuration().interfazConf);
                frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame2.pack();
                frame2.setLocationRelativeTo(null);  // *** this will center your app ***
                frame2.setVisible(true);

            }
        });
    }

    public static void visible(){
        frame.setContentPane(new MainInterface().Interfaz);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);  // *** this will center your app ***
        frame2.setVisible(false);
        frame.setVisible(true);


    }

    public static void main(String [] args) {
        visible();

    }
}