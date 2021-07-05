#! /usr/bin/env python3
import fileinput
import sys
import re

symTable = {}

operatorList = ["+", "-", "*", "/", "<", ">", ">=", "<=", "==", "!=", "num", "var", "str"]

keywordList = ["let", "if", "input", "print", "goto"]


# used to store a parsed TL expressions which are
# constant numbers, constant strings, variable names, and binary expressions
class Expr:
    def __init__(self, op1, operator, op2=None):
        self.op1 = op1
        self.operator = operator
        self.op2 = op2

    def __str__(self):
        if self.op2 == None:
            return "(" + self.operator + " " + str(self.op1) + ")"
        else:
            return "(" + self.operator + " " + str(self.op1) + " " + str(self.op2) + ")"

    # evaluate this expression given the environment of the symTable
    def eval(self, symTable, lineNum):
        # Checks if the operator is in the operatorList
        if not self.operator in operatorList:
            print("Syntax error on line " + str(lineNum + 1) + ".")
            quit()
        # checks if operator is var, str, num, or any operator
        if self.operator == "var":
            if self.op1 in symTable:
                return symTable[self.op1]
            else:
                print("Undefined variable " + self.op1 + " at line " + str(lineNum + 1) +".")
                quit()
        elif self.operator == "str":
            return self.op1[1:-1]
        elif self.operator == "num":
            return self.op1
        elif self.operator == "+":
            return float(self.op1.eval(symTable, lineNum)) + float(self.op2.eval(symTable, lineNum))
        elif self.operator == "-":
            return float(self.op1.eval(symTable, lineNum)) - float(self.op2.eval(symTable, lineNum))
        elif self.operator == "*":
            return float(self.op1.eval(symTable, lineNum)) * float(self.op2.eval(symTable, lineNum))
        elif self.operator == "/":
            return float(self.op1.eval(symTable, lineNum)) / float(self.op2.eval(symTable, lineNum))
        elif self.operator == ">":
            if float(self.op1.eval(symTable, lineNum)) > float(self.op2.eval(symTable, lineNum)):
                return 1
            else:
                return 0
        elif self.operator == "<":
            if float(self.op1.eval(symTable, lineNum)) < float(self.op2.eval(symTable, lineNum)):
                return 1
            else:
                return 0
        elif self.operator == ">=":
            if float(self.op1.eval(symTable, lineNum)) >= float(self.op2.eval(symTable, lineNum)):
                return 1
            else:
                return 0
        elif self.operator == "<=":
            if float(self.op1.eval(symTable, lineNum)) <= float(self.op2.eval(symTable, lineNum)):
                return 1
            else:
                return 0
        elif self.operator == "==":
            if float(self.op1.eval(symTable, lineNum)) == float(self.op2.eval(symTable, lineNum)):
                return 1
            else:
                return 0
        elif self.operator == "!=":
            if float(self.op1.eval(symTable, lineNum)) != float(self.op2.eval(symTable, lineNum)):
                return 1
            else:
                return 0
        else:
            return -1


# used to store a parsed TL statement
class Stmt:
    def __init__(self, keyword, exprs):
        self.keyword = keyword
        self.exprs = exprs

    def __str__(self):
        others = ""
        for exp in self.exprs:
            others = others + " " + str(exp)
        return self.keyword + others

    #prints list
    def printList(self, exprs, symTable, lineNum):
        for e in exprs:
            if e != None:
                print(str(e.eval(symTable, lineNum)), end = ' ')
        print()

    # perform/execute this statement given the environment of the symTable
    def perform(self, symTable, lineNum):
        if self.keyword == "print":
            self.printList(self.exprs, symTable, lineNum)
            # print ("Perform print")
            return lineNum + 1
        elif self.keyword == "let":
            key = self.exprs[0].op1
            value = float(self.exprs[1].eval(symTable, lineNum))
            symTable[key] = value
            return lineNum + 1
        elif self.keyword == "if":
            check = self.exprs[0].eval(symTable, lineNum)
            if check != 0:
                if self.exprs[1].op1 in symTable:
                    return symTable[self.exprs[1].op1]
                else:
                    print("Illegal goto " + self.exprs[1].op1 + " at line " + str(lineNum + 1) + ".")
                    quit()
            else:
                return lineNum + 1
        elif self.keyword == "input":
            key = self.exprs[0].op1
            inputValue = input()
            assert (inputValue.isnumeric()), "Illegal or missing input"
            symTable[key] = float(inputValue)
            return lineNum + 1
        else:
            return lineNum + 1

# checks if the word is a label
def isLabel(word):
    if len(word) < 2:
        return False
    if word[len(word) - 1] == ":":
        return True
    else:
        return False

# parses an expr
def parseExpr(words, lineNum):
    if len(words) < 1:
        return None
    if type(words) == str:
        if len(words) >= 3 and words[0] == '"' and words[len(words) - 1] == '"':
            operator = 'str'
        else:
            operator = 'var'
        expr = Expr(words, operator, None)
        return expr

    elif type(words) == list and len(words) == 1:
        if str(words[0]).isnumeric():
            operator = 'num'
        elif len(words[0]) >= 3 and words[0][0] == '"' and words[0][-1] == '"':
            operator = 'str'
        elif str(words[0]).isalnum:
            operator = 'var'
        expr = Expr(words[0], operator, None)
        return expr
    if not words[1] in operatorList:
        print("Syntax error on line " + str(lineNum + 1) + ".")
        quit()
    expr = Expr(parseExpr([words[0]], lineNum), words[1], parseExpr(words[2:], lineNum))
    return expr

# calls parseExpr on a list
def parseExprList(words, lineNum):
    exprs = []
    oneExpr = []
    for i in range(len(words)):
        token = words[i]
        if token != ',':
            oneExpr.append(token)
        else:
            expr = parseExpr(oneExpr, lineNum)
            exprs.append(expr)
            oneExpr = []
    expr = parseExpr(oneExpr, lineNum)
    exprs.append(expr)
    return exprs

# parses a statement
def parseStmt(words, lineNum):
    if not words[0] in keywordList:
        print("Syntax error on line " + str(lineNum + 1) + ".")
        quit()
    exprs = []
    if words[0] == "let":
        if str(words[1]).isalnum() and words[2] is "=":
            expr1 = Expr(words[1], 'var', None)
            expr2 = parseExpr(words[3:], lineNum)
            exprs.append(expr1)
            exprs.append(expr2)
            stmt = Stmt(words[0], exprs)
            return stmt
        else:
            return None
    elif words[0] == "print":
        exprs = parseExprList(words[1:], lineNum)
        stmt = Stmt(words[0], exprs)
        return stmt
    elif words[0] == "input":
        if str(words[1]).isalnum():
            expr = Expr(words[1], 'var', None)
            exprs.append(expr)
            stmt = Stmt(words[0], exprs)
            return stmt
        else:
            print("Illegal or missing input")
            quit()
    elif words[0] == "if":
        if "goto" in words:
            index = words.index("goto")
            expr1 = parseExpr(words[1:index], lineNum)
            expr2 = parseExpr(words[index + 1:], lineNum)
            exprs.append(expr1)
            exprs.append(expr2)
            stmt = Stmt(words[0], exprs)
            return stmt
        else:
            print("Syntax error on line " + str(lineNum + 1) + ".")
            quit()
    else:
        print("Syntax error on line " + str(lineNum + 1) + ".")
        quit()


def parseLine(words, lineNum):
    if isLabel(words[0]):
        symTable[words[0][:-1]] = lineNum
        del (words[0])
    stmt = parseStmt(words, lineNum)
    return stmt

def run(stmtList, symTable, lineNum):
    # Loop through the stmtList and call perform
    i = lineNum
    while i < len(stmtList):
        stmt = stmtList[i]
        nextLine = stmt.perform(symTable, i)
        i = nextLine

# transforms a line into a series of tokens
def line2Tokens(line):
    quotedString = re.findall(r'\".*?\"',line)
    if len(quotedString) == 0:
        tokens = [elem for elem in re.split(r'\s', line) if elem != '']
    else:
        current = 0
        tokens = []
        for string in quotedString:
            pos = line.find(string) + current
            subLine = line[current:pos - 1]
            subTokens = [elem for elem in re.split(r'\s', subLine) if elem != '']
            tokens += subTokens + [string]
            current = pos + len(string)
        if current < len(line):
            subLine = line[current:]
            subTokens = [elem for elem in re.split(r'\s', subLine) if elem != '']
            tokens += subTokens
    return tokens

if __name__ == '__main__':
    assert (len(sys.argv) >= 2), "missing tli program file name as argument."
    f = open(sys.argv[1], "r")
    source = f.readlines()
    lineNum = 0
    stmtList = []
    for lineOfWords in source:
        words = line2Tokens(lineOfWords)
        stmt = parseLine(words, lineNum)
        stmtList.append(stmt)
        lineNum = lineNum + 1
    run(stmtList, symTable, 0)