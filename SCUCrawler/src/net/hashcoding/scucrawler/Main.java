package net.hashcoding.scucrawler;

import net.hashcoding.scucrawler.task.JWCTask;

public class Main {

	public static void main(String[] args) {
		TaskManager manager = TaskManager.instance();
		manager.addTask(new JWCTask());
		manager.run();
	}
}
