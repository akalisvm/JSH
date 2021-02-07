// Generated from E:/UCL_CS/COMP0010/jsh-team-38/jsh-team-38/src/main/antlr4/uk/ac/ucl/jsh\JshGrammar.g4 by ANTLR 4.8
package com.antlr.math.generate;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link JshGrammarVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class JshGrammarBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements JshGrammarVisitor<T> {
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitCommand(JshGrammarParser.CommandContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPipe(JshGrammarParser.PipeContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAtomicCommand(JshGrammarParser.AtomicCommandContext ctx) { return visitChildren(ctx); }
}