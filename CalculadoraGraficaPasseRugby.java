package com.mycompany.trabalho_fisica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;

public class CalculadoraGraficaPasseRugby extends JFrame {
    private JTextField campoVelocJogador;
    private JTextField campoVelocBola;
    private JTextArea resultado;
    private PainelGrafico painelGrafico;
    private JLabel tituloLabel;
    
    private double anguloCalculado = 0;
    private boolean desenhar = false;
    private DecimalFormat df = new DecimalFormat("#.##");

    public CalculadoraGraficaPasseRugby() {
        setTitle("Calculadora de Passe - RUGBY");
        setSize(850, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        // Configuração do ícone
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/rugby_icon.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Ícone não encontrado, usando padrão.");
        }

        // Painel de título
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(new Color(0, 102, 0));
        tituloLabel = new JLabel("CALCULADORA DE PASSE - RUGBY");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 20));
        tituloLabel.setForeground(Color.WHITE);
        painelTitulo.add(tituloLabel);
        add(painelTitulo, BorderLayout.NORTH);
        
        // Painel do enunciado
        JTextArea areaEnunciado = new JTextArea(
            "Um jogador de rúgbi corre com a bola em direção à meta do adversário, no sentido positivo de um eixo x. " +
            "De acordo com as regras do jogo, pode passar a bola a um companheiro de equipe desde que a velocidade " +
            "da bola em relação ao campo não possua uma componente x positiva. Suponha que o jogador esteja correndo " +
            "com uma velocidade de 4,0 m/s em relação ao campo quando passa a bola com uma velocidade v_b em relação " +
            "a ele mesmo. Se o módulo de v_b é 6,0 m/s, qual é o menor ângulo que a bola deve fazer com a direção x " +
            "para que o passe seja válido?"
        );
            areaEnunciado.setWrapStyleWord(true);
            areaEnunciado.setLineWrap(true);
            areaEnunciado.setEditable(false);
            areaEnunciado.setFont(new Font("Arial", Font.ITALIC, 13));
            areaEnunciado.setBackground(new Color(250, 250, 250));
            areaEnunciado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JScrollPane scrollEnunciado = new JScrollPane(areaEnunciado);
            scrollEnunciado.setPreferredSize(new Dimension(780, 100));
            scrollEnunciado.setBorder(BorderFactory.createTitledBorder("Enunciado original: "));

            add(scrollEnunciado, BorderLayout.WEST);

        // Painel de entrada
        JPanel painelEntrada = new JPanel(new GridBagLayout());
        painelEntrada.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painelEntrada.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Componentes de entrada
        JLabel labelJogador = new JLabel("Velocidade do Jogador (m/s):");
        labelJogador.setFont(new Font("Arial", Font.PLAIN, 14));
        campoVelocJogador = new JTextField(10);
        campoVelocJogador.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel labelBola = new JLabel("Velocidade da Bola (m/s):");
        labelBola.setFont(new Font("Arial", Font.PLAIN, 14));
        campoVelocBola = new JTextField(10);
        campoVelocBola.setFont(new Font("Arial", Font.PLAIN, 14));

        // Posicionamento dos componentes
        gbc.gridx = 0; gbc.gridy = 0;
        painelEntrada.add(labelJogador, gbc);
        gbc.gridx = 1;
        painelEntrada.add(campoVelocJogador, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        painelEntrada.add(labelBola, gbc);
        gbc.gridx = 1;
        painelEntrada.add(campoVelocBola, gbc);

        // Criando botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painelBotoes.setBackground(Color.WHITE);
        
        JButton btnCalcular = criarBotao("CALCULAR", new Color(0, 102, 0), Color.WHITE);
        JButton btnResetar = criarBotao("RESETAR", new Color(204, 0, 0), Color.WHITE);
        
        painelBotoes.add(btnCalcular);
        painelBotoes.add(btnResetar);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        painelEntrada.add(painelBotoes, gbc);

        add(painelEntrada, BorderLayout.NORTH);

        // Painel de gráfico
        painelGrafico = new PainelGrafico();
        painelGrafico.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        // Painel vertical com enunciado + gráfico
        JPanel painelConteudo = new JPanel();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBackground(Color.WHITE);

        // Adiciona enunciado e gráfico ao painel
        painelConteudo.add(scrollEnunciado);
        painelConteudo.add(Box.createRigidArea(new Dimension(0, 10))); // espaço
        painelGrafico.setPreferredSize(new Dimension(780, 400));
        painelConteudo.add(painelGrafico);

        // Coloca tudo num JScrollPane
        JScrollPane scrollPaneCentro = new JScrollPane(painelConteudo);
        scrollPaneCentro.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneCentro.setBorder(BorderFactory.createTitledBorder("Visualização"));

        // Adiciona ao centro da janela
        add(scrollPaneCentro, BorderLayout.CENTER);



        // Área de resultado
        resultado = new JTextArea(4, 20);
        resultado.setFont(new Font("Arial", Font.PLAIN, 14));
        resultado.setEditable(false);
        resultado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultado.setBackground(new Color(240, 240, 240));
        JScrollPane scrollResultado = new JScrollPane(resultado);
        scrollResultado.setBorder(BorderFactory.createTitledBorder("Resultado"));
        add(scrollResultado, BorderLayout.SOUTH);

        // Ações dos botões
        btnCalcular.addActionListener((ActionEvent e) -> calcularMenorAngulo());
        btnResetar.addActionListener((ActionEvent e) -> resetarPainel());
    }

    private JButton criarBotao(String texto, Color corFundo, Color corTexto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setBackground(corFundo);
        botao.setForeground(Color.BLACK);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(corFundo.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(corFundo);
            }
        });
        
        return botao;
    }

    private void calcularMenorAngulo() {
        try {
            double vetJogador = Double.parseDouble(campoVelocJogador.getText());
            double vetBola = Double.parseDouble(campoVelocBola.getText());

            if (vetJogador <= 0 || vetBola <= 0) {
                resultado.setText("ERRO: As velocidades devem ser valores positivos.");
                desenhar = false;
                painelGrafico.repaint();
                return;
            }

            if (vetBola <= vetJogador) {
                resultado.setText("ERRO: A velocidade da bola precisa ser maior que a do jogador.");
                desenhar = false;
                painelGrafico.repaint();
                return;
            }

            double cosTheta = -vetJogador / vetBola;
            double anguloRad = Math.acos(cosTheta);
            anguloCalculado = Math.toDegrees(anguloRad);

            resultado.setText(String.format("Ângulo mínimo para o passe ser válido: %s° (graus)\n\n", df.format(anguloCalculado)) +
                String.format("Razão entre velocidades (Vj/Vb): %s", df.format(cosTheta)));
            desenhar = true;
            painelGrafico.repaint();
        } catch (NumberFormatException ex) {
            resultado.setText("ERRO: Informe valores numéricos válidos para as velocidades.");
            desenhar = false;
            painelGrafico.repaint();
        }
    }

    private void resetarPainel() {
        campoVelocJogador.setText("");
        campoVelocBola.setText("");
        resultado.setText("");
        desenhar = false;
        painelGrafico.repaint();
        campoVelocJogador.requestFocus();
    }

    private class PainelGrafico extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fundo gradiente
            GradientPaint gradient = new GradientPaint(0, 0, new Color(230, 255, 230), getWidth(), getHeight(), Color.WHITE);
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            if (!desenhar) {
                // Mensagem quando não há dados
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("Arial", Font.ITALIC, 16));
                String mensagem = "Informe as velocidades e clique em CALCULAR";
                int msgWidth = g2.getFontMetrics().stringWidth(mensagem);
                g2.drawString(mensagem, getWidth()/2 - msgWidth/2, getHeight()/2);
                return;
            }

            int centroX = getWidth() / 2;
            int centroY = getHeight() - 100;
            int raio = Math.min(getWidth(), getHeight()) / 3;

            // Eixo x (direção do jogador)
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(50, centroY, getWidth() - 50, centroY);
            
            // Flecha do eixo x
            int[] xPoints = {getWidth() - 50, getWidth() - 70, getWidth() - 70};
            int[] yPoints = {centroY, centroY - 10, centroY + 10};
            g2.fillPolygon(xPoints, yPoints, 3);
            g2.drawString("Direção do Jogador", getWidth() - 180, centroY - 15);

            // Vetor da bola
            double anguloRad = Math.toRadians(anguloCalculado);
            int x2 = centroX - (int) (raio * Math.cos(anguloRad));
            int y2 = centroY - (int) (raio * Math.sin(anguloRad));

            g2.setColor(new Color(200, 0, 0));
            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(centroX, centroY, x2, y2);
            
            // Flecha do vetor da bola
            desenharFlecha(g2, centroX, centroY, x2, y2, Color.RED);

            // Arco do ângulo
            g2.setColor(new Color(0, 100, 0, 100));
            g2.setStroke(new BasicStroke(1));
            int arcSize = 100;
            g2.drawArc(centroX - arcSize/2, centroY - arcSize/2, arcSize, arcSize, 0, (int)anguloCalculado);
            
            // Texto do menor ângulo calculado
            g2.setColor(new Color(0, 100, 0));
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            int textX = centroX + 30;
            int textY = centroY - 30;
            g2.drawString(String.format("θ = %s°", df.format(anguloCalculado)), textX + 65, textY - 65);
            
            // Legenda
            g2.setColor(Color.BLACK);
            g2.drawString("Vetor da Bola", x2 - 50, y2 - 10);
        }
        
        private void desenharFlecha(Graphics2D g2, int x1, int y1, int x2, int y2, Color cor) {
            double dx = x2 - x1, dy = y2 - y1;
            double angle = Math.atan2(dy, dx);
            int len = (int) Math.sqrt(dx*dx + dy*dy);
            int arrowLen = 15;
            
            // Linha principal
            g2.setColor(cor);
            g2.drawLine(x1, y1, x2, y2);
            
            // Ponta da flecha
            Polygon arrowHead = new Polygon();  
            arrowHead.addPoint(len, 0);
            arrowHead.addPoint(len - arrowLen, -arrowLen/2);
            arrowHead.addPoint(len - arrowLen, arrowLen/2);
            
            // Transformação para posicionar corretamente
            AffineTransform tx = AffineTransform.getTranslateInstance(x1, y1);
            tx.concatenate(AffineTransform.getRotateInstance(angle));
            Shape transformed = tx.createTransformedShape(arrowHead);
            
            g2.fill(transformed);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Usar look and feel do sistema para melhor aparência
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            CalculadoraGraficaPasseRugby frame = new CalculadoraGraficaPasseRugby();
            frame.setVisible(true);
        });
    }
}