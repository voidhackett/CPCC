package net.hashcoding.scucrawler.solver;

import java.io.IOException;

import net.hashcoding.scucrawler.utils.HtmlToMakedown;

public class HtmlToMarkdownSolver implements PageSolver {

	public String solve(String content) {
		try {
			return HtmlToMakedown.convertHtml(content, "GBK");
		} catch (IOException e) {
			// ignore
			e.printStackTrace();
			return null;
		}
	}

}
