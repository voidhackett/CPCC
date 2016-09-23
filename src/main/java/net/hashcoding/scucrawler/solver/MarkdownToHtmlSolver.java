package net.hashcoding.scucrawler.solver;

import org.markdownj.MarkdownProcessor;

public class MarkdownToHtmlSolver implements PageSolver {

	public String solve(String content) {
		MarkdownProcessor processor = new MarkdownProcessor();
		return processor.markdown(content);
	}

}
