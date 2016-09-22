package net.hashcoding.scucrawler.solver;

import java.io.IOException;

import net.hashcoding.scucrawler.utils.HtmlToMakedown;

public class HtmlToMakedownSolver implements PageSolver {

	@Override
	public String solve(String content) {
		try {
			return HtmlToMakedown.convertHtml(content, "GBK");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
