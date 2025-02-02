/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package figuras;

import ingenieria1202410.PanelDeDibujo;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JPanel;


public class BaldeDePintura extends Figura{

    ArrayList<Point> puntos;
    private PanelDeDibujo panelDeDibujo;

    public BaldeDePintura(PanelDeDibujo panel, Color color) {
        panelDeDibujo = (PanelDeDibujo) panel;
        this.puntos = new ArrayList<>();
        this.colorDePrimerPlano = color;
    }
    
    public void rellenarPorDifusion( Point puntoActual, Color color) {
        int x = puntoActual.x;
        int y = puntoActual.y;
        
        BufferedImage imagen = new BufferedImage(panelDeDibujo.getWidth(), panelDeDibujo.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D grafico = imagen.createGraphics();
        panelDeDibujo.paint(grafico);
        grafico.dispose();
        
        int srcColor = imagen.getRGB(x, y);
        boolean[][] hits = new boolean[imagen.getHeight()][imagen.getWidth()];

        Queue<Point> cola = new LinkedList<Point>();
        cola.add(new Point(x, y));

        while (!cola.isEmpty()) {
            Point p = cola.remove();

            if (debeRellenarse(imagen, hits, p.x, p.y, srcColor, color.getRGB())) {
                cola.add(new Point(p.x, p.y - 1));
                cola.add(new Point(p.x, p.y + 1));
                cola.add(new Point(p.x - 1, p.y));
                cola.add(new Point(p.x + 1, p.y));
                puntos.add(p);
             
            }
        }
        
    }

    private static boolean debeRellenarse(BufferedImage imagen, boolean[][] hits, int x, int y, int colorFuente, int colorObjetivo) {
        if (y < 0) {
            return false;
        }
        if (x < 0) {
            return false;
        }
        if (y > imagen.getHeight() - 1) {
            return false;
        }
        if (x > imagen.getWidth() - 1) {
            return false;
        }

        if (hits[y][x]) {
            return false;
        }

        if (imagen.getRGB(x, y) != colorFuente) {
            return false;
        }

        // valid, paint it
        imagen.setRGB(x, y, colorObjetivo);
        hits[y][x] = true;
        
        
        return true;
    }

//    public void dibujar(Graphics g) {
//        g.setColor(colorDePrimerPlano);
//        for (Point punto : puntos) {
//            g.drawLine(punto.x, punto.y, punto.x, punto.y);
//        }
//    }

    @Override
    public void actualizar(Point puntoActual) {
        rellenarPorDifusion(puntoActual, colorDePrimerPlano);
    }

    @Override
    public void dibujar(Graphics2D g) {
       g.setColor(colorDePrimerPlano);
        for (Point punto : puntos) {
            g.drawLine(punto.x, punto.y, punto.x, punto.y);
        }
    }
}