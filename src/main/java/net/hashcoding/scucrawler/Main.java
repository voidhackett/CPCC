package net.hashcoding.scucrawler;

import net.hashcoding.scucrawler.task.*;

public class Main {
	public static String dumpFilename;
	public static int FactoryEmployeeSize = 5;
	public static Thread MainThread;

	public static void main(String[] args) {
        dumpFilename = "D:\\passage_dump.txt";

		MainThread = Thread.currentThread();

        // leancloud sdk init.
        Config.initialize();
        PageFactory.instance().start();

		TaskManager manager = TaskManager.instance();
		manager.delegateTask(new JWCAnnounceTask());
		manager.delegateTask(new JWCTextNewsTask());
		manager.delegateTask(new XGBAnnounceTask());
		manager.delegateTask(new XGBTextNewsTask());
		manager.delegateTask(new QCCDAnnounceTask());
		manager.delegateTask(new QCCDTextNewsTask());
		manager.run();
		PageFactory.instance().waitFactoryStop();

        System.out.println("Now wait all worker thread stop...");
	}
}
