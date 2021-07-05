// Jason Zheng
// December 6, 2019
// CSE 112

import scala.collection.mutable.ListBuffer
import scala.io.{Source, StdIn}
import scala.collection.mutable.Map

abstract class Expr
case class Var(name: String) extends Expr
case class Str(name: String) extends Expr
case class Constant(num: Double) extends Expr
case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

abstract class Stmt
case class Let(variable: String, expr: Expr) extends Stmt
case class If(expr: Expr, label: String) extends Stmt
case class Input(variable: String) extends Stmt
case class Print(exprList: List[Expr]) extends Stmt

object TLI {
  //Creates a template for evaluating expressions
  def eval(expr: Expr, symTab: Map[String, Double], lineNum:Double): Double = expr match {
    case BinOp("+", e1, e2) => eval(e1, symTab, lineNum) + eval(e2, symTab, lineNum)
    case BinOp("-", e1, e2) => eval(e1, symTab, lineNum) - eval(e2, symTab, lineNum)
    case BinOp("*", e1, e2) => eval(e1, symTab, lineNum) * eval(e2, symTab, lineNum)
    case BinOp("/", e1, e2) => eval(e1, symTab, lineNum) / eval(e2, symTab, lineNum)
    case BinOp(">", e1, e2) => if (eval(e1, symTab, lineNum) > eval(e2, symTab, lineNum)) 1 else 0
    case BinOp("<", e1, e2) => if (eval(e1, symTab, lineNum) < eval(e2, symTab, lineNum)) 1 else 0
    case BinOp(">=", e1, e2) => if (eval(e1, symTab, lineNum) >= eval(e2, symTab, lineNum)) 1 else 0
    case BinOp("<=", e1, e2) => if (eval(e1, symTab, lineNum) <= eval(e2, symTab, lineNum)) 1 else 0
    case BinOp("==", e1, e2) => if (eval(e1, symTab, lineNum) == eval(e2, symTab, lineNum)) 1 else 0
    case BinOp("!=", e1, e2) => if (eval(e1, symTab, lineNum) != eval(e2, symTab, lineNum)) 1 else 0
    // Executes exception if the variable is not in the symTable
    case Var(name) => {
      if(symTab.contains(name)) {
        symTab(name)
      }
      else{
        println("Undefined variable " + name + " in line " + (lineNum + 1).toString)
        System.exit(-1)
        return -1.0
      }
    }
    case Constant(num) => num

    //error handling
    case _ => println("Syntax error on line " + (lineNum + 1).toString + ".")
      System.exit(-1)
      -1.0
  }

  def printList(exprs: List[Expr], symTab: Map[String, Double], lineNum: Double): Unit = {
    // Goes through the expressions
    for (expr <- exprs) {
      // Checks if expr is string
      expr match {
        case Str(name) => print(name.substring(1, name.length - 1) + " ")
        case _ => {
          print(eval(expr, symTab, lineNum).toString + " ")
        }
      }
    }
    println("")
  }

  //checks stmt and performs it based on what it is
  def perform(stmt: Stmt, symTab: Map[String, Double], lineNum: Double): Double = {
    stmt match {
      // Adds variable and value to symTable
      case Let(variable, expr) => {
        val value = eval(expr, symTab, lineNum)
        symTab(variable) = value
        return lineNum + 1
      }
      // Prints list
      case Print(exprList) => {
        printList(exprList, symTab, lineNum)
        return lineNum + 1
      }
      // Records user input
      case Input(variable) => {
        //Check if input is integer
        try {
          val value = StdIn.readInt().toDouble
          symTab(variable) = value
          return lineNum + 1
        }
        // error handling
        catch{
          case e: Exception => println("Illegal or missing input")
            System.exit(-1)
            return -1
        }
      }
      // Checks if label is in the symTable
      case If(expr, label) => {
        if (eval(expr, symTab, lineNum) == 1.0) {
          if(symTab.contains(label)) {
            val lineVal = symTab(label)
            return lineVal
          }
          else{
            println("Illegal goto " + label + " at line " + (lineNum + 1))
            System.exit(-1)
            -1
          }
        }
        else {
          return lineNum + 1
        }
      }
      case _ => {
        //error handling
        println("Syntax error on line " + (lineNum + 1) + ".")
        System.exit(-1)
        return -1
      }
    }
  }

  //Runs through program
  def run(stmtList:List[Stmt], symTab:Map[String, Double], lineNum:Double): Unit = {
    var curLine = lineNum
    while(curLine < stmtList.length){
      val nextLine = perform(stmtList(curLine.toInt), symTab, curLine)
      curLine = nextLine
    }
  }

  def parseExpr(tokens: List[String], lineNum: Double): Expr = {

    val operators: List[String] = List("+", "-", "*", "/", "==", "!=", ">", "<", ">=", "<=")

    //single token expr
    if (tokens.size == 1) {
      //const number expr
      if (tokens.head.matches("\\d+")) {
        return new Constant(tokens.head.toDouble)
      }

      //const string expr
      if ((tokens.head.size >= 3) && (tokens.head.charAt(0) == '"') && (tokens.head.charAt(tokens.head.size - 1) == '"')) {
        return new Str(tokens.head)
      }

      //var expr
      if (tokens.head.matches("\\w+")) {
        return new Var(tokens.head)
      }

      //exception handling
      println("Syntax error on line " + (lineNum + 1).toInt.toString + ".")
      System.exit(-1)
    }

    //3-token expr
    if ((tokens.size == 3) && (operators.contains(tokens(1)))) {
      return new BinOp(tokens(1), parseExpr(List(tokens(0)), lineNum), parseExpr(List(tokens(2)), lineNum))
    }

    //exception handling
    println("Syntax error on line " + (lineNum + 1).toInt.toString + ".")
    System.exit(-1)
    return new Str("none")

  }

  def parseExprList(exprListTokens: List[String], lineNum: Double): List[Expr] = {
    // initialize 2 empty lists: returnExprList, curExprTokens
    val returnExprList = ListBuffer[Expr]()
    val curExprTokens = ListBuffer[String]()
    var i = 0
    
    //Loop over exprListTokens one at a time
    for (i <- 0 to exprListTokens.length - 1) {
      if (exprListTokens(i) == ",") {
        val curExpr = parseExpr(curExprTokens.toList, lineNum)
        returnExprList += curExpr
        curExprTokens.clear()
      }
      else if (i == exprListTokens.length - 1) {
        curExprTokens += exprListTokens(i)
        val curExpr = parseExpr(curExprTokens.toList, lineNum)
        returnExprList += curExpr
        curExprTokens.clear()
      }
      else {
        curExprTokens += exprListTokens(i)
      }
    }

    //Return returnExprList
    return returnExprList.toList
  }

  def parseStmt(stmtTokens: List[String], lineNum: Double): Stmt = {

    //Make sure stmtTokens(0) comes with the proper keyword
    val keyword: List[String] = List("let", "print", "if", "input")
    if (!keyword.contains(stmtTokens(0))) {
      println("Syntax error on line " + (lineNum + 1).toInt.toString + ".")
      System.exit(-1)
    }

    // parses statements based upon keyword
    val exprTokens = ListBuffer[String]()
    if (stmtTokens(0) == "let") {
      val varName = stmtTokens(1)
      exprTokens ++= stmtTokens.slice(3, stmtTokens.length)
      val newExpr = parseExpr(exprTokens.toList, lineNum)
      return new Let(varName, newExpr)
    }
    else if (stmtTokens(0) == "print") {
      exprTokens ++= stmtTokens.slice(1, stmtTokens.length)
      val newExprList: List[Expr] = parseExprList(exprTokens.toList, lineNum)
      return new Print(newExprList)
    }
    else if (stmtTokens(0) == "input") {
      val varName = stmtTokens(1)
      return new Input(varName)
    }
    else if (stmtTokens(0) == "if") {
      val varName = stmtTokens(1)
      if (stmtTokens.contains("goto")) {
        val idxGoto = stmtTokens.indexOf("goto")
        exprTokens ++= stmtTokens.slice(1, idxGoto)
        val label = stmtTokens(idxGoto + 1)
        return new If(parseExpr(exprTokens.toList, lineNum), label)
      }
      else {
        println("Syntax error on line " + (lineNum + 1).toInt.toString + ".")
        System.exit(-1)
      }
    }
    else {
      println("Syntax error on line " + (lineNum + 1).toInt.toString + ".")
      System.exit(-1)
    }

    return new Input("None")
  }

  def lines2Tokens(lines: List[String]): List[List[String]] = {
    val returnTokens = ListBuffer[List[String]]()
    
    // parse input one line at a time
    for (curLine <- lines) {
      // remove quotations
      val re = "\".*?\"".r
      var curIndex = 0
      val lineTokens = ListBuffer[String]()
      for (quoteString <- re.findAllIn(curLine)) {
        val quoteIndex = curLine.indexOf(quoteString)
        val quoteFree = curLine.substring(curIndex, quoteIndex - 1)
        val tokenList: List[String] = quoteFree.split("\\s+").filter(_ != "").toList
        for (token <- tokenList) {
          lineTokens += token
        }
        lineTokens += quoteString
        curIndex += quoteIndex + quoteString.length()
      }
      if (curIndex < curLine.length()) {
        val quoteFree = curLine.substring(curIndex)
        val tokenList: List[String] = quoteFree.split("\\s+").filter(_ != "").toList
        for (token <- tokenList) {
          lineTokens += token
        }
      }
      returnTokens += lineTokens.toList
    }

    return returnTokens.toList
  }

  def isLabel(token: String): Boolean = {
    // Checks the size of the token
    if (token.size >= 2) {
      // Checks if there is a colon
      if (token.charAt(token.size - 1) == ':') {
        return true
      }
    }
    return false
  }
  
  // parses an input line
  def parseLine(inputLine: List[String], symTab: Map[String, Double], lineNum: Double): Stmt = {
    // checks if the first string is a label
    if (isLabel(inputLine(0))) {
      val fstToken = inputLine(0)
      val modLine = inputLine.tail
      val label = fstToken.substring(0, fstToken.size - 1)
      //add the label and lineNum to the symTable
      symTab += (label -> lineNum)
      return parseStmt(modLine, lineNum)
    }
    else {
      return parseStmt(inputLine, lineNum)
    }
  }

  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      println("\"tli\" expects a program file name as command line argument")
      System.exit(-1)
    }
    val filename = args(0)
    val source = Source.fromFile(filename)
    val linesAsList = source.getLines.toList

    val tokensList = lines2Tokens(linesAsList)

    var symTab: Map[String, Double] = Map[String, Double]()
    var lineNum: Double = 0.0
    val stmtList = ListBuffer[Stmt]()
    for (line <- tokensList) {
      stmtList += parseLine(line, symTab, lineNum)
      lineNum += 1
    }

    run(stmtList.toList, symTab, 0)
  }
}