package juego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Juego extends JFrame {

	private static final long serialVersionUID = 1L;
	private Cuadrado cuadroSeleccionado;
	private List<Cuadrado> cuadros;

	/**
	 * Create the frame.
	 */
	public Juego() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 600);
		setLocationRelativeTo(null);
		JPanel panelPrincipal = new JPanel(new GridLayout(4, 4));

		cuadros = new ArrayList<Cuadrado>();

		for (int i = 1; i <= 8; i++) {
			Color color = obtenerColorAleatorio();
			Cuadrado cuadrado1 = new Cuadrado(color);
			Cuadrado cuadrado2 = new Cuadrado(color);
			cuadros.add(cuadrado1);
			cuadros.add(cuadrado2);
		}

		Collections.shuffle(cuadros);

		for (Cuadrado c : cuadros) {
			c.addMouseListener(new CuadroMouseListener());
			panelPrincipal.add(c);
		}

		add(panelPrincipal);

	}

	private Color obtenerColorAleatorio() {
		float r = (float) Math.random();
		float g = (float) Math.random();
		float b = (float) Math.random();
		return new Color(r, g, b);
	}

	private void mostrarDialogoAcierto() {
		JOptionPane.showMessageDialog(Juego.this, "¡Acertaste!");

	}
	
    private boolean todosRevelados() {
        for (Cuadrado c : cuadros) {
            if (!c.revelado) {
                return false;
            }
        }
        return true;
    }
    
    private void mostrarMensajeGanador() {
        JOptionPane.showMessageDialog(Juego.this, "¡Has ganado el juego!");
    }

	class Cuadrado extends JPanel {

		private boolean revelado;
		private Color colorCuadrado;

		public Cuadrado(Color color) {
			this.colorCuadrado = color;
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}

		public Color getImagen() {
			return colorCuadrado;
		}

		public void revelar() {
			this.revelado = true;
			repaint();
		}

		public void ocultar() {
			this.revelado = false;
			repaint();
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (revelado) {
				g.setColor(colorCuadrado);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		}
	}

	// Clase interna para manejar eventos del ratón en los cuadros
	private class CuadroMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			Cuadrado cuadro = (Cuadrado) e.getSource();

			if (!cuadro.revelado) {
				cuadro.revelar();

				if (cuadroSeleccionado == null) {
					cuadroSeleccionado = cuadro;
				} else {
					// Comprobar si los colores coinciden
					if (cuadroSeleccionado.colorCuadrado.equals(cuadro.colorCuadrado)) {
						mostrarDialogoAcierto();
	                    cuadroSeleccionado = null;

					}else {
	                  // Ocultar los cuadros después de un breve tiempo
						Timer timer = new Timer(500, new ActionListener() {
	                        @Override
	                        public void actionPerformed(ActionEvent arg0) {
	                            cuadroSeleccionado.ocultar();
	                            cuadro.ocultar();
	                            cuadroSeleccionado = null;
	                        }
	                    });
	                    timer.setRepeats(false);
	                    timer.start();
	                    /*new Thread(() -> {
							try {
								Thread.sleep(1000);
							} catch (Exception e2) {
								// TODO: handle exception
							}
							if (cuadroSeleccionado != null) {
	                            cuadroSeleccionado.ocultar();
	                            cuadro.ocultar();
	                            cuadroSeleccionado = null;
	                        }
						}).start();
	                    */
	                    
	                 // Verificar si todos los cuadros han sido revelados
	                    

					}
					if (todosRevelados()) {
                        mostrarMensajeGanador();
                    }
				}
			}
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Juego juego = new Juego();
			juego.setVisible(true);

		});
	}

}
