package net.turrem.viewer;

public class ViewerMain
{
	public static void main(String[] args)
	{
		String dir = System.getProperty("user.dir");
		dir = dir.replaceAll("\\\\", "\\/");
		dir += "/";
		String file = null;
		if (args.length > 0)
		{
			file = args[0];
		}
		Viewer viewer = new Viewer(file, dir);
		viewer.run();
	}
}
