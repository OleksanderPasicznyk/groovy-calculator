package pl.rozart.groovycalculator

import groovy.swing.SwingBuilder

import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JTextField
import java.awt.BorderLayout
import java.awt.Font
import java.awt.GraphicsEnvironment
import java.text.NumberFormat


public class CalculatorGUI {

    private Stack<Object> calculatorStack = new Stack<>();
    private String displayString = "0";

    private SwingBuilder swingBuilder = new SwingBuilder()
    private Font buttonFont = new Font("Impact", Font.BOLD, 30)

    private Font calculatorFont
    private JFrame frame
    private JTextField display

    private GroovyShell groovyShell = new GroovyShell()


    public static void main(String[] args) {
        CalculatorGUI calculator = new CalculatorGUI();
        calculator.loadFont()
        calculator.buildGui()
    }

    private void loadFont() {
        File fontFile = new File("resources/fonts/joystix_monospace.ttf")
        URL fontUrl = fontFile.toURI().toURL()
        calculatorFont = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream())
        calculatorFont = calculatorFont.deriveFont(Font.PLAIN, 30)
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
                display = textField(text: "0", horizontalAlignment: JTextField.CENTER_ALIGNMENT, font: calculatorFont, background: java.awt.Color.LIGHT_GRAY);
            }
            panel(constraints: BorderLayout.CENTER, background: java.awt.Color.LIGHT_GRAY) {
                gridLayout(cols: 5, rows: 4, vgap: 10, hgap: 10)

                button(text: "7", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    addDigitToDisplay("7")
                })
                button(text: "8", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    addDigitToDisplay("8")
                })
                button(text: "9", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    addDigitToDisplay("9")
                })
                button(text: "/", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    addOperationToStack("/")
                })
                button(text: "*", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    addOperationToStack("*")
                })

                button(text: "4", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    addDigitToDisplay("4")
                })
                button(text: "5", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    addDigitToDisplay("5")
                })
                button(text: "6", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    addDigitToDisplay("6")
                })
                button(text: "-", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    addOperationToStack("-")
                })
                button(text: "+", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    addOperationToStack("+")
                })

                button(text: "1", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    addDigitToDisplay("1")
                })
                button(text: "2", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    addDigitToDisplay("2")
                })
                button(text: "3", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    addDigitToDisplay("3")
                })
                button(text: "C", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    clearAll()
                })
                button(text: "CE", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    clearDisplay()
                })

                button(text: ".", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    addDecimalPoint()
                })
                button(text: "0", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    addDigitToDisplay("0")
                })
                button(text: "=", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {
                    try{
                        calculate(false)
                    }catch (ArithmeticException e){
                        clearAll()
                    }
               })
            }


        }
    }

    private void addDigitToDisplay(String digit) {
        if (calculatorStack.size() == 2) {
            if (displayString.equals("0")) {
                displayString = digit
                println "Replaced zero with " + digit
            } else if (displayString.endsWith(".0")) {
                displayString = displayString.substring(0, displayString.length() - 1) + digit
                println "Replaced decimal zero with " + digit
            } else {
                displayString += digit
                println "Added digit " + digit + " to existing one"
            }
        } else {
            if (displayString.equals("0")) {
                displayString = digit
                println "Replaced zero with " + digit
            } else if (displayString.endsWith(".0")) {
                displayString = displayString.substring(0, displayString.length() - 1) + digit
                println "Replaced decimal zero with " + digit
            } else {
                displayString += digit
                println "Added digit " + digit + " to existing one"
            }
        }
        refreshDisplay()
    }

    private void addOperationToStack(String operation) {
        if (calculatorStack.size() == 2 && !displayString.isEmpty()) {
            try {
                calculate(true)
                calculatorStack.push(operation)
                displayString = ""
                println "Calculated and pushed operation: " + operation
            } catch (ArithmeticException e) {
                clearAll()
            }
        } else if (calculatorStack.empty()) {
            BigDecimal number = new BigDecimal(displayString)
            calculatorStack.push(number)
            calculatorStack.push(operation)
            displayString = ""
            println "Pushed operation: " + operation
        }
    }

    private BigDecimal calculate(Boolean isAfterOperation) throws ArithmeticException {
        if (calculatorStack.size() == 2) {
            BigDecimal number = new BigDecimal(displayString)
            calculatorStack.push(number)

            String calculateString = "";
            for (Object calculatorElement : calculatorStack) {
                calculateString += calculatorElement.toString()
            }

            BigDecimal result = groovyShell.evaluate(calculateString)

            displayString = result.toString()
            refreshDisplay()

            calculatorStack = new Stack<>()
            if (isAfterOperation) {
                calculatorStack.push(result)
            }
            println "Result is: " + displayString


        }
    }

    private void clearAll() {
        calculatorStack = new Stack<>()
        displayString = "0";
        refreshDisplay()
        println "Cleaned completely"
    }

    private void clearDisplay() {
        if (calculatorStack.size() == 2 && !displayString.equals("0")) {
            displayString = "0"
            refreshDisplay()
            println "Cleaned last operation"
        } else {
            clearAll()
        }
    }

    private addDecimalPoint() {
        if (!displayString.contains(".") && displayString.matches("\\d+")) {
            displayString += ".0";
            refreshDisplay();
            println "Decimal point added"
        }
    }

    private refreshDisplay() {
        display.setText(displayString)
        println calculatorStack
    }

}