package stanhebben.minetweaker.script;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import stanhebben.minetweaker.script.parser.CompiledDFA;
import stanhebben.minetweaker.script.parser.NFA;
import stanhebben.minetweaker.script.parser.Token;
import stanhebben.minetweaker.script.parser.TokenStream;

public class TweakerParser extends TokenStream {
	private static final HashMap<String, Integer> KEYWORDS;
	
	public static final int T_ID = 1;
	public static final int T_INTVALUE = 2;
	public static final int T_FLOATVALUE = 3;
	public static final int T_STRINGVALUE = 4;
	public static final int T_AOPEN = 5;
	public static final int T_ACLOSE = 6;
	public static final int T_SQBROPEN = 7;
	public static final int T_SQBRCLOSE = 8;
	public static final int T_DOT2 = 9;
	public static final int T_DOT = 10;
	public static final int T_COMMA = 11;
	public static final int T_PLUSASSIGN = 12;
	public static final int T_PLUS = 13;
	public static final int T_MINUSASSIGN = 14;
	public static final int T_MINUS = 15;
	public static final int T_MULASSIGN = 16;
	public static final int T_MUL = 17;
	public static final int T_DIVASSIGN = 18;
	public static final int T_DIV = 19;
	public static final int T_MODASSIGN = 20;
	public static final int T_MOD = 21;
	public static final int T_ORASSIGN = 22;
	public static final int T_OR = 23;
	public static final int T_OR2 = 40;
	public static final int T_ANDASSIGN = 24;
	public static final int T_AND2 = 41;
	public static final int T_AND = 25;
	public static final int T_XORASSIGN = 26;
	public static final int T_XOR = 27;
	public static final int T_QUEST = 28;
	public static final int T_COLON = 29;
	public static final int T_BROPEN = 30;
	public static final int T_BRCLOSE = 31;
	public static final int T_TILDE = 32;
	public static final int T_SEMICOLON = 33;
	public static final int T_LTEQ = 34;
	public static final int T_LT = 35;
	public static final int T_GTEQ = 36;
	public static final int T_GT = 37;
	public static final int T_EQ = 38;
	public static final int T_ASSIGN = 39;
	public static final int T_NOTEQ = 42;
	public static final int T_NOT = 43;
	public static final int T_DOLLAR = 44;
	
	public static final int T_BOOL = 100;
	public static final int T_BYTE = 101;
	public static final int T_SHORT = 102;
	public static final int T_INT = 103;
	public static final int T_LONG = 104;
	public static final int T_FLOAT = 105;
	public static final int T_DOUBLE = 106;
	public static final int T_STRING = 107;
	public static final int T_FUNCTION = 108;
	public static final int T_IN = 109;
	
	public static final int T_AS = 120;
	public static final int T_VERSION = 121;
	public static final int T_IF = 122;
	public static final int T_ELSE = 123;
	public static final int T_FOR = 124;
	public static final int T_RETURN = 125;
	public static final int T_INCLUDE = 126;
	
	public static final int T_NULL = 140;
	public static final int T_TRUE = 141;
	public static final int T_FALSE = 142;
	
	
	private static final String[] REGEXPS = {
		"#[^\n]*\n",
		"//[^\n]*\n",
		"[ \t\r\n]*",
		"[a-zA-Z_][a-zA-Z_0-9]*",
		"\\-?(0|[1-9][0-9]*)\\.[0-9]+([eE][\\+\\-]?[0-9]+)?",
        "\\-?(0|[1-9][0-9]*)",
		"\"([^\"\\\\]|\\\\([\"\\\\/bfnrt]|u[0-9a-fA-F]{4}))*\"",
        "\\{",
        "\\}",
        "\\[",
        "\\]",
        "\\.\\.",
        "\\.",
        ",",
        "\\+=",
        "\\+",
        "\\-=",
        "\\-",
        "\\*=",
        "\\*",
        "/=",
        "/",
        "%=",
        "%",
        "\\|=",
        "\\|\\|",
        "\\|",
        "&=",
        "&&",
        "&",
        "\\^=",
        "\\^",
        "\\?",
        ":",
        "\\(",
        "\\)",
        "~",
        ";",
        "<=",
        "<",
        ">=",
        ">",
        "==",
        "=",
        "!=",
        "!",
		"$"
	};
	private static final int[] FINALS = {
		-1,
		-1,
		-1,
		T_ID,
		T_FLOATVALUE,
		T_INTVALUE,
		T_STRINGVALUE,
		T_AOPEN,
		T_ACLOSE,
		T_SQBROPEN,
		T_SQBRCLOSE,
		T_DOT2,
		T_DOT,
		T_COMMA,
		T_PLUSASSIGN,
		T_PLUS,
		T_MINUSASSIGN,
		T_MINUS,
		T_MULASSIGN,
		T_MUL,
		T_DIVASSIGN,
		T_DIV,
		T_MODASSIGN,
		T_MOD,
		T_ORASSIGN,
		T_OR2,
		T_OR,
		T_ANDASSIGN,
		T_AND2,
		T_AND,
		T_XORASSIGN,
		T_XOR,
		T_QUEST,
		T_COLON,
		T_BROPEN,
		T_BRCLOSE,
		T_TILDE,
		T_SEMICOLON,
		T_LTEQ,
		T_LT,
		T_GTEQ,
		T_GT,
		T_EQ,
		T_ASSIGN,
		T_NOTEQ,
		T_NOT,
		T_DOLLAR
	};
	private static final CompiledDFA DFA = new NFA(REGEXPS, FINALS).toDFA().optimize().compile();
	
	static {
		KEYWORDS = new HashMap<String, Integer>();
		KEYWORDS.put("bool", T_BOOL);
		KEYWORDS.put("byte", T_BYTE);
		KEYWORDS.put("short", T_SHORT);
		KEYWORDS.put("int", T_INT);
		KEYWORDS.put("long", T_LONG);
		KEYWORDS.put("float", T_FLOAT);
		KEYWORDS.put("double", T_DOUBLE);
		KEYWORDS.put("string", T_STRING);
		KEYWORDS.put("function", T_FUNCTION);
		KEYWORDS.put("in", T_IN);
		
		KEYWORDS.put("as", T_AS);
		KEYWORDS.put("version", T_VERSION);
		KEYWORDS.put("if", T_IF);
		KEYWORDS.put("else", T_ELSE);
		KEYWORDS.put("for", T_FOR);
		KEYWORDS.put("return", T_RETURN);
		KEYWORDS.put("include", T_INCLUDE);
		
		KEYWORDS.put("null", T_NULL);
		KEYWORDS.put("true", T_TRUE);
		KEYWORDS.put("false", T_FALSE);
	}
	
	public TweakerParser(String file, Reader contents) throws IOException {
		super(file, contents, DFA);
	}
	
	public TweakerParser(String file, String contents) throws IOException {
		super(file, new StringReader(contents), DFA);
	}
	
	@Override
	public Token process(Token token) {
		if (token.getType() == T_ID) {
			if (KEYWORDS.containsKey(token.getValue())) {
				return new Token(token.getValue(), KEYWORDS.get(token.getValue()), token.getFile(), token.getLine(), token.getLineOffset());
			}
		}
		return token;
	}
}
