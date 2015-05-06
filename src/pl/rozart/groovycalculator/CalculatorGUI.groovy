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

    private Stack<Object> calcStack = new Stack<>();

    private SwingBuilder swingBuilder = new SwingBuilder()
    private Font buttonFont = new Font("Impact", Font.BOLD, 30)

    private Font calculatorFont
    private JFrame frame
    private JTextField display

    private GroovyShell groovyShell = new GroovyShell()

    private String lastDoubleString = "";

    public static void main(String[] args){
        CalculatorGUI calculator = new CalculatorGUI();
        calculator.loadFont()
        calculator.buildGui()
    }

    private void loadFont(){
        File fontFile = new File("resources/fonts/joystix_monospace.ttf")
        URL fontUrl = fontFile.toURI().toURL()
        calculatorFont = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream())
        calculatorFont = calculatorFont.deriveFont(Font.PLAIN,30)
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

                button(text: "7", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction(7L)})
                button(text: "8", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction(8L)})
                button(text: "9", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction(9L)})
                button(text: "/", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("/")})
                button(text: "*", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("*")})

                button(text: "4", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction(4L)})
                button(text: "5", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction(5L)})
                button(text: "6", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction(6L)})
                button(text: "-", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("-")})
                button(text: "+", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("+")})

                button(text: "1", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction(1L)})
                button(text: "2", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction(2L)})
                button(text: "3", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction(3L)})
                button(text: "C", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("C")})
                button(text: "CE", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("CE")})

                button(text: ".", horizontalTextPosition: JButton.CENTER, font: buttonFont, actionPerformed: {calculatorAction(".")})
                button(text: "0", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction(0L)})
                button(text: "=", horizontalTextPosition: JButton.CENTER, font:  buttonFont, actionPerformed: {calculatorAction("=")})
            }


        }
    }

    private void calculatorAction(Object actionValue){
        if(actionValue instanceof Number) {
            if (calcStack.isEmpty()) {
                calcStack.push(actionValue)
                display.setText(actionValue.toString())
            } else {
                if (calcStack.peek() instanceof Long) {
                    actionForLong(actionValue)
                }else if(calcStack.peek() instanceof Double){
                    actionForDouble(actionValue)
                }else {
                    calcStack.push(actionValue)
                    display.setText(actionValue.toString())
                }
            }
        }else{
            String action = (String) actionValue
            addOperation(action)
        }
    }

    private void actionForLong(Number selectedNumber){
        Long number = calcStack.pop()
        try {
            number = Long.parseLong(number.toString() + selectedNumber)
        }catch(NumberFormatException e){
            println "Number is too long"
        }
        calcStack.push(number)
        display.setText(number.toString())
    }

    private void actionForDouble(Number selectedNumber){
        Double numberFromStack =  calcStack.pop()
        String numberFromStackAsString = numberFromStack.toString()
        if(numberFromStackAsString.endsWith(".0")){
            numberFromStackAsString = numberFromStackAsString.substring(0, numberFromStackAsString.length() - 1)
        }
        if(!lastDoubleString.isEmpty()){
            numberFromStackAsString = lastDoubleString
        }
        numberFromStackAsString += selectedNumber
        if(selectedNumber == 0){
            lastDoubleString = numberFromStackAsString
        }else{
            lastDoubleString = ""
        }
        numberFromStack = Double.parseDouble(numberFromStackAsString)
        calcStack.push(numberFromStack)
        display.setText(numberFromStackAsString)
    }

    private void addOperation(String operation){
        switch(operation){
            case "C":
                calcStack = new Stack<>()
                display.setText("0")
                break;
            case "CE":
                if(!calcStack.empty()){
                    Object lastStackObject = calcStack.pop()
                    if(lastStackObject instanceof String){
                        display.setText(calcStack.peek())
                    }else if(!calcStack.empty()){
                        lastStackObject = calcStack.pop()
                        display.setText(calcStack.peek())
                        calcStack.push(lastStackObject)
                    }else{
                        calcStack = new Stack<>()
                        display.setText("0")
                    }
                }
                break;
            case "=":
                calculateResult()
                break;
            case ".":
                if(!calcStack.empty()){
                    Object lastStackObject = calcStack.pop()
                    if(lastStackObject instanceof Long){
                        Double newStackObject = lastStackObject.doubleValue()
                        calcStack.push(newStackObject)
                        display.setText(newStackObject.toString())
                    }
                }
                break;
            default:
                if(!calcStack.empty() && calcStack.peek() instanceof  Number){
                    calcStack.push(operation)
                }
        }
    }

    private Long calculateResult(){
        if(isStackReadyForCalculation()){

            String rowToCalculate = ""
            for(Object stackObject : calcStack) {
                rowToCalculate += stackObject
            }

            try {
                Number result = groovyShell.evaluate(rowToCalculate)
                calcStack = new Stack<>()
                calcStack.push(result)
                display.setText(result.toString())
            }catch(ArithmeticException e){
                println "Attempt of division by zero!"
                calcStack = new Stack<>()
                display.setText("0")
            }

        }
    }

    private boolean isStackReadyForCalculation(){
        if(calcStack.size() > 1 && calcStack.size() % 2 == 1){
            return true;
        }
        return false;
    }




}