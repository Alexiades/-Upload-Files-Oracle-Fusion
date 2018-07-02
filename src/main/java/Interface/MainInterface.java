package Interface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainInterface {
    private JButton runButton;
    private JButton configurationButton;
    private JPanel Interfaz;
    static JFrame frame = new JFrame("MainInterface");


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
                Configuration conf = new Configuration();
                conf.visible();
            }
        });
    }

    public static void visible(){
        frame.setContentPane(new MainInterface().Interfaz);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);  // *** this will center your app ***
        frame.setVisible(true);

    }

    public static void main(String [] args) {
        visible();

    }
}