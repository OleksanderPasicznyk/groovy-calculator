package pl.rozart.groovycalculator

import groovy.swing.SwingBuilder

import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JTextField
import java.awt.BorderLayout
import java.awt.Font
import java.awt.GraphicsEnvironment


public class CalculatorGUI {

    private SwingBuilder swingBuilder = new SwingBuilder()
    private Font buttonFont = new Font("Impact", Font.BOLD, 30)

    private Font calculatorFont
    private JFrame frame
    private JTextField display

    public static void main(String[] args){
        CalculatorGUI calculator = new CalculatorGUI();
        calculator.loadFont()
        calculator.buildGui()
    }

    private void loadFont(){
        File fontFile = new File("resources/fonts/LiquidCrystal-Normal.ttf")
        URL fontUrl = fontFile.toURI().toURL()
        calculatorFont = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream())
        calculatorFont = calculatorFont.deriveFont(Font.PLAIN,45)
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment()
        graphicsEnvironment.registerFont(calculatorFont)
    }

    private void buildGui() {

        frame = swingBuilder.frame(title: 'Groovy Calculator',
                location: [400, 50],
                size: [600, 200],
                pack: true,
                show: true,
                defaultCloseOperation: JFrame.EXIT_ON_CLOSE) {
            panel(constraints: BorderLayout.NORTH) {
                borderLayout(hgap: 10, vgap: 5)
                display = textField(text: "0", horizontalAlignment: JTextField.CENTER_ALIGNMENT, font: calculatorFont,  background: java.awt.Color.LIGHT_GRAY);
            }
            panel(constraints: BorderLayout.CENTER, background: java.awt.Color.LIGHT_GRAY) {
                gridLayout(cols: 5, rows: 4, vgap: 10, hgap: 10)

                button(text: "7", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("7")})
                button(text: "8", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("8")})
                button(text: "9", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("9")})
                button(text: "/", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("/")})
                button(text: "*", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("*")})

                button(text: "4", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("4")})
                button(text: "5", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("5")})
                button(text: "6", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("6")})
                button(text: "-", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("-")})
                button(text: "+", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("+")})

                button(text: "1", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("1")})
                button(text: "2", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("2")})
                button(text: "3", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("3")})
                button(text: "C", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("C")})
                button(text: "CE", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("CE")})

                button(text: "0", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("0")})
                button(text: "=", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("=")})
            }


        }
    }

    private void calculatorAction(String actionValue){
        this.display.setText(actionValue);
    }






}