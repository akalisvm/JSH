// Generated from E:/UCL_CS/COMP0010/jsh-team-38/jsh-team-38/src/main/antlr4/uk/ac/ucl/jsh\JshGrammar.g4 by ANTLR 4.8
package com.antlr.math.generate;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link JshGrammarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface JshGrammarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link JshGrammarParser#command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommand(JshGrammarParser.CommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link JshGrammarParser#pipe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPipe(JshGrammarParser.PipeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JshGrammarParser#atomicCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomicCommand(JshGrammarParser.AtomicCommandContext ctx);
}