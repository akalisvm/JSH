// Generated from E:/UCL_CS/COMP0010/jsh-team-38/jsh-team-38/src/main/antlr4/uk/ac/ucl/jsh\JshGrammar.g4 by ANTLR 4.8
package com.antlr.math.generate;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JshGrammarParser}.
 */
public interface JshGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link JshGrammarParser#command}.
	 * @param ctx the parse tree
	 */
	void enterCommand(JshGrammarParser.CommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link JshGrammarParser#command}.
	 * @param ctx the parse tree
	 */
	void exitCommand(JshGrammarParser.CommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link JshGrammarParser#pipe}.
	 * @param ctx the parse tree
	 */
	void enterPipe(JshGrammarParser.PipeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JshGrammarParser#pipe}.
	 * @param ctx the parse tree
	 */
	void exitPipe(JshGrammarParser.PipeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JshGrammarParser#atomicCommand}.
	 * @param ctx the parse tree
	 */
	void enterAtomicCommand(JshGrammarParser.AtomicCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link JshGrammarParser#atomicCommand}.
	 * @param ctx the parse tree
	 */
	void exitAtomicCommand(JshGrammarParser.AtomicCommandContext ctx);
}