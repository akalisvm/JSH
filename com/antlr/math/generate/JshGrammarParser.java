// Generated from E:/UCL_CS/COMP0010/jsh-team-38/jsh-team-38/src/main/antlr4/uk/ac/ucl/jsh\JshGrammar.g4 by ANTLR 4.8
package com.antlr.math.generate;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JshGrammarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, NONSPECIAL=3, DOUBLEQUOTED=4, SINGLEQUOTED=5, BACKQUOTED=6;
	public static final int
		RULE_command = 0, RULE_pipe = 1, RULE_atomicCommand = 2;
	private static String[] makeRuleNames() {
		return new String[] {
			"command", "pipe", "atomicCommand"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'|'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "NONSPECIAL", "DOUBLEQUOTED", "SINGLEQUOTED", "BACKQUOTED"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "JshGrammar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public JshGrammarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class CommandContext extends ParserRuleContext {
		public List<AtomicCommandContext> atomicCommand() {
			return getRuleContexts(AtomicCommandContext.class);
		}
		public AtomicCommandContext atomicCommand(int i) {
			return getRuleContext(AtomicCommandContext.class,i);
		}
		public CommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_command; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JshGrammarListener ) ((JshGrammarListener)listener).enterCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JshGrammarListener ) ((JshGrammarListener)listener).exitCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JshGrammarVisitor ) return ((JshGrammarVisitor<? extends T>)visitor).visitCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommandContext command() throws RecognitionException {
		CommandContext _localctx = new CommandContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_command);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(6);
			atomicCommand();
			setState(11);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(7);
				match(T__0);
				setState(8);
				atomicCommand();
				}
				}
				setState(13);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PipeContext extends ParserRuleContext {
		public List<AtomicCommandContext> atomicCommand() {
			return getRuleContexts(AtomicCommandContext.class);
		}
		public AtomicCommandContext atomicCommand(int i) {
			return getRuleContext(AtomicCommandContext.class,i);
		}
		public PipeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pipe; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JshGrammarListener ) ((JshGrammarListener)listener).enterPipe(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JshGrammarListener ) ((JshGrammarListener)listener).exitPipe(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JshGrammarVisitor ) return ((JshGrammarVisitor<? extends T>)visitor).visitPipe(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PipeContext pipe() throws RecognitionException {
		PipeContext _localctx = new PipeContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_pipe);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
			atomicCommand();
			setState(19);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(15);
				match(T__1);
				setState(16);
				atomicCommand();
				}
				}
				setState(21);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomicCommandContext extends ParserRuleContext {
		public List<TerminalNode> NONSPECIAL() { return getTokens(JshGrammarParser.NONSPECIAL); }
		public TerminalNode NONSPECIAL(int i) {
			return getToken(JshGrammarParser.NONSPECIAL, i);
		}
		public List<TerminalNode> DOUBLEQUOTED() { return getTokens(JshGrammarParser.DOUBLEQUOTED); }
		public TerminalNode DOUBLEQUOTED(int i) {
			return getToken(JshGrammarParser.DOUBLEQUOTED, i);
		}
		public List<TerminalNode> SINGLEQUOTED() { return getTokens(JshGrammarParser.SINGLEQUOTED); }
		public TerminalNode SINGLEQUOTED(int i) {
			return getToken(JshGrammarParser.SINGLEQUOTED, i);
		}
		public List<TerminalNode> BACKQUOTED() { return getTokens(JshGrammarParser.BACKQUOTED); }
		public TerminalNode BACKQUOTED(int i) {
			return getToken(JshGrammarParser.BACKQUOTED, i);
		}
		public AtomicCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomicCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JshGrammarListener ) ((JshGrammarListener)listener).enterAtomicCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JshGrammarListener ) ((JshGrammarListener)listener).exitAtomicCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JshGrammarVisitor ) return ((JshGrammarVisitor<? extends T>)visitor).visitAtomicCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomicCommandContext atomicCommand() throws RecognitionException {
		AtomicCommandContext _localctx = new AtomicCommandContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_atomicCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(23); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(22);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NONSPECIAL) | (1L << DOUBLEQUOTED) | (1L << SINGLEQUOTED) | (1L << BACKQUOTED))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(25); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NONSPECIAL) | (1L << DOUBLEQUOTED) | (1L << SINGLEQUOTED) | (1L << BACKQUOTED))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\b\36\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\3\2\3\2\3\2\7\2\f\n\2\f\2\16\2\17\13\2\3\3\3\3\3\3\7\3\24"+
		"\n\3\f\3\16\3\27\13\3\3\4\6\4\32\n\4\r\4\16\4\33\3\4\2\2\5\2\4\6\2\3\3"+
		"\2\5\b\2\35\2\b\3\2\2\2\4\20\3\2\2\2\6\31\3\2\2\2\b\r\5\6\4\2\t\n\7\3"+
		"\2\2\n\f\5\6\4\2\13\t\3\2\2\2\f\17\3\2\2\2\r\13\3\2\2\2\r\16\3\2\2\2\16"+
		"\3\3\2\2\2\17\r\3\2\2\2\20\25\5\6\4\2\21\22\7\4\2\2\22\24\5\6\4\2\23\21"+
		"\3\2\2\2\24\27\3\2\2\2\25\23\3\2\2\2\25\26\3\2\2\2\26\5\3\2\2\2\27\25"+
		"\3\2\2\2\30\32\t\2\2\2\31\30\3\2\2\2\32\33\3\2\2\2\33\31\3\2\2\2\33\34"+
		"\3\2\2\2\34\7\3\2\2\2\5\r\25\33";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}