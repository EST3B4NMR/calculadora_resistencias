import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class CalculadoraVisual {
    // Variables autogeneradas
    private JPanel panelPrincipal;
    private JButton btnCalcular;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JLabel lblResultado;
    private JPanel panelDibujo; // Nuestro nuevo lienzo

    // Diccionario global de colores
    private static final Map<String, Color> MAPA_COLORES = new HashMap<>();
    static {
        MAPA_COLORES.put("Negro", Color.BLACK);
        MAPA_COLORES.put("Marrón", new Color(139, 69, 19));
        MAPA_COLORES.put("Rojo", Color.RED);
        MAPA_COLORES.put("Naranja", Color.ORANGE);
        MAPA_COLORES.put("Amarillo", Color.YELLOW);
        MAPA_COLORES.put("Verde", Color.GREEN);
        MAPA_COLORES.put("Azul", Color.BLUE);
        MAPA_COLORES.put("Violeta", new Color(148, 0, 211));
        MAPA_COLORES.put("Gris", Color.GRAY);
        MAPA_COLORES.put("Blanco", Color.WHITE);
        MAPA_COLORES.put("Dorado", new Color(212, 175, 55));
        MAPA_COLORES.put("Plateado", new Color(192, 192, 192));
    }

    public CalculadoraVisual() {
        // 1. Configurar listas
        String[] coloresValores = {"Negro", "Marrón", "Rojo", "Naranja", "Amarillo", "Verde", "Azul", "Violeta", "Gris", "Blanco"};
        String[] coloresMultiplicador = {"Negro", "Marrón", "Rojo", "Naranja", "Amarillo", "Verde", "Azul", "Dorado", "Plateado"};
        String[] coloresTolerancia = {"Marrón", "Rojo", "Verde", "Azul", "Violeta", "Gris", "Dorado", "Plateado"};

        comboBox1.setModel(new DefaultComboBoxModel<>(coloresValores));
        comboBox2.setModel(new DefaultComboBoxModel<>(coloresValores));
        comboBox3.setModel(new DefaultComboBoxModel<>(coloresMultiplicador));
        comboBox4.setModel(new DefaultComboBoxModel<>(coloresTolerancia));

        comboBox1.setRenderer(new PintorDeColores());
        comboBox2.setRenderer(new PintorDeColores());
        comboBox3.setRenderer(new PintorDeColores());
        comboBox4.setRenderer(new PintorDeColores());

        // Agregamos nuestra silueta al lienzo
        SiluetaResistencia dibujo = new SiluetaResistencia();
        panelDibujo.setLayout(new BorderLayout());
        panelDibujo.add(dibujo, BorderLayout.CENTER);

        // Crear el "escuchador" que actualizará el dibujo al seleccionar un color
        ActionListener actualizadorDibujo = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color c1 = MAPA_COLORES.getOrDefault((String) comboBox1.getSelectedItem(), Color.BLACK);
                Color c2 = MAPA_COLORES.getOrDefault((String) comboBox2.getSelectedItem(), Color.BLACK);
                Color c3 = MAPA_COLORES.getOrDefault((String) comboBox3.getSelectedItem(), Color.BLACK);
                Color c4 = MAPA_COLORES.getOrDefault((String) comboBox4.getSelectedItem(), Color.BLACK);
                dibujo.actualizarBandas(c1, c2, c3, c4);
            }
        };

        // Asignamos el escuchador a las 4 listas
        comboBox1.addActionListener(actualizadorDibujo);
        comboBox2.addActionListener(actualizadorDibujo);
        comboBox3.addActionListener(actualizadorDibujo);
        comboBox4.addActionListener(actualizadorDibujo);

        // Disparar el primer dibujo por defecto
        comboBox1.setSelectedItem("Marrón");
        // operaciones

        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Valores de las bandas (1 y 2)
                Map<String, Integer> valores = new HashMap<>();
                valores.put("Negro", 0); valores.put("Marrón", 1); valores.put("Rojo", 2);
                valores.put("Naranja", 3); valores.put("Amarillo", 4); valores.put("Verde", 5);
                valores.put("Azul", 6); valores.put("Violeta", 7); valores.put("Gris", 8); valores.put("Blanco", 9);

                // Multiplicadores
                Map<String, Double> multiplicadores = new HashMap<>();
                multiplicadores.put("Negro", 1.0); multiplicadores.put("Marrón", 10.0);
                multiplicadores.put("Rojo", 100.0); multiplicadores.put("Naranja", 1000.0);
                multiplicadores.put("Amarillo", 10000.0); multiplicadores.put("Verde", 100000.0);
                multiplicadores.put("Azul", 1000000.0); multiplicadores.put("Dorado", 0.1); multiplicadores.put("Plateado", 0.01);

                // Tolerancias
                Map<String, String> tolerancias = new HashMap<>();
                tolerancias.put("Marrón", "±1%"); tolerancias.put("Rojo", "±2%");
                tolerancias.put("Verde", "±0.5%"); tolerancias.put("Azul", "±0.25%");
                tolerancias.put("Violeta", "±0.1%"); tolerancias.put("Gris", "±0.05%");
                tolerancias.put("Dorado", "±5%"); tolerancias.put("Plateado", "±10%");

                try {
                    // Obtener colores seleccionados
                    String c1 = (String) comboBox1.getSelectedItem();
                    String c2 = (String) comboBox2.getSelectedItem();
                    String c3 = (String) comboBox3.getSelectedItem();
                    String c4 = (String) comboBox4.getSelectedItem();

                    // Calcular
                    int valorBase = (valores.get(c1) * 10) + valores.get(c2);
                    double resultado = valorBase * multiplicadores.get(c3);
                    String tol = tolerancias.getOrDefault(c4, "±20%");

                    // Formatear para que se vea bonito (kΩ, MΩ)
                    String unidad = "Ω";
                    if (resultado >= 1000000) {
                        resultado = resultado / 1000000;
                        unidad = "MΩ";
                    } else if (resultado >= 1000) {
                        resultado = resultado / 1000;
                        unidad = "kΩ";
                    }

                    // Mostrar en el JLabel
                    lblResultado.setText(String.format("Resultado: %.2f %s %s", resultado, unidad, tol));
                } catch (Exception ex) {
                    lblResultado.setText("Error en el cálculo");
                }

            }

        });

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Calculadora de Resistencias");
        frame.setContentPane(new CalculadoraVisual().panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500); // Lo hacemos un poco más alto
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // --- CLASE QUE DIBUJA LA SILUETA DE LA RESISTENCIA ---
    static class SiluetaResistencia extends JPanel {
        private Color banda1 = Color.BLACK, banda2 = Color.BLACK;
        private Color banda3 = Color.BLACK, banda4 = Color.BLACK;

        public void actualizarBandas(Color c1, Color c2, Color c3, Color c4) {
            this.banda1 = c1; this.banda2 = c2;
            this.banda3 = c3; this.banda4 = c4;
            repaint(); // Pide volver a dibujar con los nuevos colores
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();

            // Dibujar el alambre (línea gris)
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setStroke(new BasicStroke(6));
            g2d.drawLine(20, alto / 2, ancho - 20, alto / 2);

            // Dibujar el cuerpo de la resistencia (Azul claro similar a tu imagen)
            int anchoCuerpo = 240;
            int altoCuerpo = 80;
            int x = (ancho - anchoCuerpo) / 2;
            int y = (alto - altoCuerpo) / 2;

            g2d.setColor(new Color(0, 170, 255));
            g2d.fillRoundRect(x - 20, y - 10, 40, altoCuerpo + 20, 30, 30); // Borde izquierdo redondeado
            g2d.fillRoundRect(x + anchoCuerpo - 20, y - 10, 40, altoCuerpo + 20, 30, 30); // Borde derecho
            g2d.fillRect(x, y, anchoCuerpo, altoCuerpo); // Centro rectilíneo

            // Dibujar las franjas de colores
            int anchoFranja = 15;
            int espacio = 35;

            // Banda 1
            g2d.setColor(banda1);
            g2d.fillRect(x + 20, y, anchoFranja, altoCuerpo);
            // Banda 2
            g2d.setColor(banda2);
            g2d.fillRect(x + 20 + espacio, y, anchoFranja, altoCuerpo);
            // Banda 3 (Multiplicador)
            g2d.setColor(banda3);
            g2d.fillRect(x + 20 + (espacio * 2), y, anchoFranja, altoCuerpo);
            // Banda 4 (Tolerancia) - Va más separada al final
            g2d.setColor(banda4);
            g2d.fillRect(x + anchoCuerpo - 35, y, anchoFranja, altoCuerpo);
        }
    }

    // --- CLASE QUE PINTA LAS PALABRAS EN LA LISTA DESPLEGABLE ---
    static class PintorDeColores extends DefaultListCellRenderer {
        public PintorDeColores() { setOpaque(true); }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value != null) {
                String nombreColor = (String) value;
                Color fondo = MAPA_COLORES.getOrDefault(nombreColor, Color.WHITE);
                setBackground(fondo);
                if (nombreColor.equals("Negro") || nombreColor.equals("Azul") || nombreColor.equals("Marrón")) {
                    setForeground(Color.WHITE);
                } else {
                    setForeground(Color.BLACK);
                }
            }
            return this;
        }
    }
}