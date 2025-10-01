import java.util.Scanner;

public class MiniProject2 {
  static void debugStringArray(String[] array) {
    String text = "";
    for(int i = 0; i < array.length; i++){
      String item = array[i];
      if(array.length - 1 == i){
        text += item;
      }else{
        text += item + ", ";
      }
    }
    println(" - [" + text + "]");
  }
  static void debugDoubleArray(double[] array) {
    String text = "";
    for(int i = 0; i < array.length; i++){
      double item = array[i];
      if(array.length - 1 == i){
        text += item;
      }else{
        text += item + ", ";
      }
    }
    println(" - [" + text + "]");
  }
  static void debugIntArray(int[] array) {
    String text = "";
    for(int i = 0; i < array.length; i++){
      int item = array[i];
      if(array.length - 1 == i){
        text += item;
      }else{
        text += item + ", ";
      }
    }
    println(" - [" + text + "]");
  }
  
  static double substituteVariable(String snippet) {
    snippet = snippet.toLowerCase();
    
    switch(snippet){
      case "π":
      case "pi": return 3.141592653589793;
      case "ans": return lastAnswer;
    }
    
    throwError("Unknown variable: \"" + snippet + "\"");
    
    return 0.0;
  }
  
  static double callFunction(String funcName, double inputValue){
    switch(funcName){
      case "√":
      case "sqrt":  return Math.sqrt(inputValue);
      case "sin":   return Math.sin(inputValue);
      case "cos":   return Math.cos(inputValue);
      case "tan":   return Math.tan(inputValue);
      case "fact":  if(inputValue % 1 != 0) throwError("Factorial of rational numbers are floored.");
                    return factorial((int) inputValue);
      case "floor": return Math.floor(inputValue);
      case "round": return Math.round(inputValue);
      case "ceil":  return Math.ceil(inputValue);    
      case "log":   return Math.log(inputValue); 
      case "abs":   return Math.abs(inputValue);      
    }
  
    throwError("Unknown function: \"" + funcName + "\"");
  
    return 0.0;
  }
  
  static void throwError(String errorMessage) {
    println("\033[31mERROR: " + errorMessage);
    println("This may have unintended affects.\033[0m");
  }
  
  static String[] splitPerScope(String equation) {
    String[] output = {};
    String buffer = "";
    int scope = 0;
    for(int i = 0; i<equation.length(); i++){
      String chr = String.valueOf(equation.charAt(i));
      if(chr.equals("(")){
        buffer += "(";
        scope++;
      }else if(chr.equals(")")){
        buffer += ")";
        scope--;
      }else if(chr.equals(" ") && scope == 0){
        output = appendStringArray(output, buffer);
        buffer = "";
      }else{
        buffer += chr;
      }
    }
    output = appendStringArray(output, buffer);
    
    if(scope != 0){
      throwError("Extra or missing parenthesis.");
    }
    
    return output;
  }
  
  static String[] appendStringArray(String[] baseArray, String item) {
    String[] newArray = new String[baseArray.length + 1];
    for(int i = 0; i<baseArray.length; i++){newArray[i] = baseArray[i];}
    newArray[baseArray.length] = item;
    return newArray;
  }
  
  static double[] appendDoubleArray(double[] baseArray, double item) {
    double[] newArray = new double[baseArray.length + 1];
    for(int i = 0; i<baseArray.length; i++){newArray[i] = baseArray[i];}
    newArray[baseArray.length] = item;
    return newArray;
  }
  
  static int[] appendIntArray(int[] baseArray, int item) {
    int[] newArray = new int[baseArray.length + 1];
    for(int i = 0; i<baseArray.length; i++){newArray[i] = baseArray[i];}
    newArray[baseArray.length] = item;
    return newArray;
  }
  
  static String[] removeStringAt(String[] base, int index) {
    String[] newArray = new String[base.length - 1];
    int offset = 0;
    for(int i = 0; i<base.length; i++){
      if(i == index){
        offset--;
        continue;
      }
      newArray[i + offset] = base[i];
    }
    
    return newArray;
  }
  static double[] removeDoubleAt(double[] base, int index) {
    double[] newArray = new double[base.length - 1];
    int offset = 0;
    for(int i = 0; i<base.length; i++){
      if(i == index){
        offset--;
        continue;
      }
      newArray[i + offset] = base[i];
    }
    
    return newArray;
  }
  
  static boolean isDouble(String str) {
    try {
      Double.parseDouble(str);
      return true; 
    } catch (NumberFormatException e) {
      return false; 
    }
  }
  static boolean isInt(String str) {
    try {
      Integer.parseInt(str);
      return true; 
    } catch (NumberFormatException e) {
      return false; 
    }
  }
  
  static String trim(String base) {
    String output = "";
    for(int i = 1; i<base.length() - 1; i++){
      output += String.valueOf(base.charAt(i));
    }
    return output;
  }
  
  static double customMath(String expression) {
    String[] rawParts = splitPerScope(expression);
    String[] parts = {};
    for(int i = 0; i<rawParts.length; i++){
      String item = rawParts[i];
      if(item.length() != 0){
        parts = appendStringArray(parts, item);
      }
    }
    
    double[] values = {};
    String[] operations = {};
    String debugFunctions = "";
    
    for(int i = 0; i<parts.length; i++){
      if(i % 2 == 0){
        String snippet = parts[i];
        double value;
        if(snippet.charAt(snippet.length() - 1) == ')'){
          int j = 0;
          for(; j < snippet.length(); j++)
            if(snippet.charAt(j) == '(') break;
                       
          double evaluatedValue = customMath(trim(snippet.substring(j)));
          String funcName = snippet.substring(0, j);
          if(funcName.equals("")){
            value = evaluatedValue;
          }else{
            value = callFunction(funcName, evaluatedValue);
            if(debugOperation) debugFunctions += " - " + snippet.substring(0, j) + " -> " + snippet.substring(j) + "\n";
          }
        }else{
          if(isDouble(snippet)){ 
            value = Double.parseDouble(snippet);
          }else{ 
            value = substituteVariable(snippet);
          }
        }
        values = appendDoubleArray(values, value);
      }else{
        operations = appendStringArray(operations, parts[i]);
      }
    }
    
    if(debugOperation){
      println("\n - >>> \"" + expression + "\"");
      print(debugFunctions);
      debugDoubleArray(values);
      debugStringArray(operations);
    }
    
    for(int i = 0; i<operations.length; i++){
      String op = operations[i];
      if(op.equals("^")){
        double value1 = values[i];
        double value2 = values[i + 1];
        operations = removeStringAt(operations, i);
        values[i + 1] = customPow(value1, value2);
        values = removeDoubleAt(values, i);
        i--;
      }
    }
    for(int i = 0; i<operations.length; i++){
      String op = operations[i];
      if(op.equals("*") || op.equals("/")){
        double value1 = values[i];
        double value2 = values[i + 1];
        operations = removeStringAt(operations, i);
        if(op.equals("*")){
          values[i + 1] = value1 * value2;
        }else{
          values[i + 1] = value1 / value2;
        }
        values = removeDoubleAt(values, i);
        i--;
      }
    }
    for(int i = 0; i<operations.length; i++){
      String op = operations[i];
      if(op.equals("+") || op.equals("-")){
        double value1 = values[i];
        double value2 = values[i + 1];
        operations = removeStringAt(operations, i);
        if(op.equals("+")){
          values[i + 1] = value1 + value2;
        }else{
          values[i + 1] = value1 - value2;
        }        
        values = removeDoubleAt(values, i);
        i--;
      }
    }
    
    if(values.length == 0){
      throwError("Empty field");
      return 0.0;
    }
    
    if(debugOperation) print(" - >>> " + values[0] + "\n");
    return values[0];
  }
  
  static double lastAnswer = 0.0;
  static boolean debugOperation = true;
  static Scanner inputObj = new Scanner(System.in);
  
  public static void main(String args[]) {
    new Thread(() -> {
    while(true){
      clearConsole();
      
      System.out.println(Math.sqrt(144.0));
      
      println("Choose an action:");
      println("(1) Regular Calculator");
      println("(2) Get Factors");
      println("(3) Simplify Fraction");
      println("(4) Simplify Square Root");
      println("(5) Unit Converter");
      println("(6) Percentage Converter");
      println("(7) Time Converter");
      
      print("\nEnter your action: ");
      int action = inputInt(inputObj.nextLine());
      
      print("\n\n\n");
      
      switch(action){
        case 1: calculatorFunc(); break;
        case 2: getFactorsFunc(); break;
        case 3: simplifyFractionFunc(); break;
        case 4: simplifySquareRootFunc(); break;
        case 5: unitConverterFunc(); break;
        case 6: percentageConverterFunc(); break;
        case 7: timeConverterFunc(); break;
        default: print("Invalid action.");
      }
    }
    }).start();
  }
  
  static void calculatorFunc() {
    while(true){
      clearConsole();
    
      println("Make sure the parts are seperated, example:");
      println("• 12 + 7");
      println("• (50 / 8) + 7");
      println("• pi + (7)");
      println("• floor(sin(pi / 2) * 20)");
      println("\nVariables are supported:");
      println("• pi or π");
      println("• ans (The last value outputed = " + lastAnswer + ")");
      println("\nOperations supported:");
      println("• + (Addition)");
      println("• - (Subtraction)");
      println("• * (Multiplication)");
      println("• / (Division)");
    
      println("\n\nEnter your expression: ");
      print(" > ");
      lastAnswer = customMath(inputObj.nextLine());
      println("\n = " + (lastAnswer + 1 - 1));
     
      print("\n\nExit? (y/n): ");
      String action = inputObj.nextLine();
      if("y".equalsIgnoreCase(action)) break;
    }
  }
  
  static void getFactorsFunc() {
    while(true){
      clearConsole();
      
      println("Only takes whole numbers, rational numbers are floored.");
      
      print("\n\nEnter your number: ");
      int[] factors = getFactors(inputInt(inputObj.nextLine()));
      
      for(int factor : factors){
        print(" - " + factor);
        if(isPrime(factor)) print(" (Prime)");
        print("\n");
      }
      
      print("\n\nExit? (y/n): ");
      String action = inputObj.nextLine();
      if("y".equalsIgnoreCase(action)) break;
    }
  }
  
  static void simplifyFractionFunc() {
    while(true){
      clearConsole();
      
      println("Takes proper and improper fractions and simplifies it.");
      println("Example:");
      println("• 3/6 -> 1/2");
      println("• 4/3 -> 1 1/3 (Mixed)");
            
      print("\n\nEnter your fraction: ");
      
      String inputFraction = inputObj.nextLine();
      String[] inputParts = inputFraction.trim().split("/");
      
      int numerator = 1;
      if(inputParts.length == 0){
        throwError("Missing values.");
      }else if(isInt(inputParts[0].trim())){
        numerator = inputInt(inputParts[0]);
      }else throwError("Invalid numerator.");
      
      int denominator = 1;
      if(inputParts.length == 1){
        throwError("Missing denominator.");
      }else if(inputParts.length > 2){
        throwError("Extra values.");
      }
      if(inputParts.length >= 2){
        if(isInt(inputParts[1].trim())){
          denominator = inputInt(inputParts[1]);
        }else throwError("Invalid denominator.");
      }
      
      int gcf = getGreatestCommonFactor(numerator, denominator);
      
      int newNumerator = numerator / gcf;
      int newDenominator = denominator / gcf;
      int wholePart = 0;
      if(newDenominator <= newNumerator){
        wholePart = newNumerator / newDenominator;
        newNumerator %= newDenominator;
      }
      
      if(wholePart == 0){ 
        println(" - " + newNumerator + "/" + newDenominator);
      }else{ 
        print(" - " + wholePart);
        if(newNumerator != 0) print(" " + newNumerator + "/" + newDenominator);
        print("\n");
      }
      
      print("\n\nExit? (y/n): ");
      String action = inputObj.nextLine();
      if("y".equalsIgnoreCase(action)) break;
    }
  }
  
  static void simplifySquareRootFunc() {
    while(true){
      clearConsole();
      println("Turns a square root to its simplest form, example:");
      println("• √50 -> 5√2");
      println("• √17 -> √17");
      
      print("\nEnter your square root: √");
      int rawRoot = inputInt(inputObj.nextLine());
      
      int[] factors = getFactors(rawRoot);
      int whole = 1;
      int rootPart = 1;
      
      for(int i = factors.length - 1; i >= 0; i--){
        int factor = factors[i];
        if(isPerfectSquare(factor)){
          rootPart = rawRoot / factor;
          whole = (int) Math.sqrt(factor);
          break;
        }
      }
      
      println(" - " + whole + "√" + rootPart);
      
      if(whole == 1){
        println("√" + rootPart);
      }else if(rootPart == 1){
        println(whole);
      }else{
        println(whole + "√" + rootPart);
      }
      
      print("\n\nExit? (y/n): ");
      String action = inputObj.nextLine();
      if("y".equalsIgnoreCase(action)) break;
    }
  }
  
  static int getGreatestCommonFactor(int value1, int value2) {
    int[] factors1 = getFactors(value1);
    for(int i = factors1.length - 1; i >= 0; i--)
      if(value2 % factors1[i] == 0) return factors1[i];
    return 1;
  }
  
  static int[] getFactors(int value) {
    int[] factors = new int[0];
    for(int i = 1; i < value + 1; i++)
      if(value % i == 0)
        factors = appendIntArray(factors, i);
    return factors;
  }
  
  static boolean isPrime(int value) {
    if(value <= 1) return false;
    if(value == 2) return true;
    if(value % 2 == 0) return false;
    for(int i = 3; i * i <= value; i += 2)
      if(value % i == 0) return false;
    return true;
  }
  
  static boolean isPerfectSquare(int value) {
    if(Math.sqrt(value) % 1.0 < 0.0000001) return true;
    return false;
  }
  
  static int inputInt(String raw) {
    raw = raw.trim();
    if(isInt(raw)) return Integer.parseInt(raw);  
    if(isDouble(raw)) return (int) Double.parseDouble(raw);       
    throwError("Not a number."); 
    return 0;
  }
  
  static double inputDouble(String raw) {
    raw = raw.trim();
    if(isDouble(raw)) return Double.parseDouble(raw);   
    if(isInt(raw)) return (double) Integer.parseInt(raw);   
    throwError("Not a number.");
    return 0.0;
  }
  
  static void println(double inp) {System.out.println(inp);}
  static void println(int inp) {System.out.println(inp);}
  static void println(String inp) {System.out.println(inp);}
  static void print(String inp) {System.out.print(inp);}
  static void print(int inp) {System.out.print(inp);}
  
  static void clearConsole() {
    print("\033[H\033[2J");
    print("\n\n\n\n\n\n\n\n\n\n");
    print("\033[H\033[2J");
  }
  
  static double factorial(int value) {
    if(value < 0){
      throwError("Factorial cannot be negative.");
      return 0.0;
    }
    double fact = 1;
    for(int i = 2; i <= value; i++) fact *= i;
    return fact;
  }
  
  static double customPow(double base, double exponent) {
    if (base == 0.0) {
      if (exponent == 0.0) return 1.0;
      return 0.0;
    }

    if (base > 0 || exponent == Math.floor(exponent)) {
      double result = Math.pow(base, exponent);
      if (base < 0 && ((int) exponent) % 2 != 0) {
        result = -result;
      }
      return result;
    }

    throwError("Invalid operation: negative base with non-integer exponent.");
    return Double.NaN;
  }
  
  // ---------------- NEW FUNCTIONS NIGGER ----------------
  static void unitConverterFunc() {
    while(true){
      clearConsole();
      println("Unit Converter:");
      println("(1) Meters to Kilometers");
      println("(2) Kilometers to Meters");
      println("(3) Celsius to Fahrenheit");
      println("(4) Fahrenheit to Celsius");
      println("(5) Kilograms to Pounds");
      println("(6) Pounds to Kilograms");
      
      print("\nChoose conversion: ");
      int choice = inputInt(inputObj.nextLine());
      
      print("Enter value: ");
      double value = inputDouble(inputObj.nextLine());
      
      double result = 0.0;
      String unitOut = "";
      
      switch(choice){
        case 1: result = value / 1000; unitOut = " km"; break;
        case 2: result = value * 1000; unitOut = " m"; break;
        case 3: result = (value * 9/5) + 32; unitOut = " °F"; break;
        case 4: result = (value - 32) * 5/9; unitOut = " °C"; break;
        case 5: result = value * 2.20462; unitOut = " lbs"; break;
        case 6: result = value / 2.20462; unitOut = " kg"; break;
        default: println("Invalid choice."); continue;
      }
      
      println("Result: " + result + unitOut);
      
      print("\nExit Unit Converter? (y/n): ");
      String action = inputObj.nextLine();
      if("y".equalsIgnoreCase(action)) break;
    }
  }

  static void percentageConverterFunc() {
    while(true){
      clearConsole();
      println("Percentage Converter:");
      println("(1) Number to Percentage of Another");
      println("(2) Percentage to Decimal");
      println("(3) Decimal to Percentage");
      
      print("\nChoose conversion: ");
      int choice = inputInt(inputObj.nextLine());
      
      double result = 0.0;
      switch(choice){
        case 1:
          print("Enter part: ");
          double part = inputDouble(inputObj.nextLine());
          print("Enter whole: ");
          double whole = inputDouble(inputObj.nextLine());
          result = (part / whole) * 100;
          println("Result: " + result + "%");
          break;
        case 2:
          print("Enter percentage: ");
          double percent = inputDouble(inputObj.nextLine());
          result = percent / 100;
          println("Result: " + result);
          break;
        case 3:
          print("Enter decimal: ");
          double decimal = inputDouble(inputObj.nextLine());
          result = decimal * 100;
          println("Result: " + result + "%");
          break;
        default:
          println("Invalid choice.");
      }
      
      print("\nExit Percentage Converter? (y/n): ");
      String action = inputObj.nextLine();
      if("y".equalsIgnoreCase(action)) break;
    }
  }
  
  static void timeConverterFunc() {
    while(true){
      clearConsole();
      println("Time Converter:");
      println("(1) Seconds to Minutes");
      println("(2) Minutes to Hours");
      println("(3) Hours to Days");
      println("(4) Days to Hours");
      
      print("\nChoose conversion: ");
      int choice = inputInt(inputObj.nextLine());
      
      print("Enter value: ");
      double value = inputDouble(inputObj.nextLine());
      
      double result = 0.0;
      String unitOut = "";
      
      switch(choice){
        case 1: result = value / 60; unitOut = " min"; break;
        case 2: result = value / 60; unitOut = " hr"; break;
        case 3: result = value / 24; unitOut = " days"; break;
        case 4: result = value * 24; unitOut = " hr"; break;
        default: println("Invalid choice."); continue;
      }
      
      println("Result: " + result + unitOut);
      
      print("\nExit Time Converter? (y/n): ");
      String action = inputObj.nextLine();
      if("y".equalsIgnoreCase(action)) break;
    }
  }
}
